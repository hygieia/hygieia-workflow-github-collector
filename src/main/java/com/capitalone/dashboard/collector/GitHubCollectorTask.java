package com.capitalone.dashboard.collector;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.CollectionError;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.CommitType;
import com.capitalone.dashboard.model.GitHub;
import com.capitalone.dashboard.model.GitRequest;
import com.capitalone.dashboard.model.Workflow;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.CommitRepository;
import com.capitalone.dashboard.repository.ComponentRepository;
import com.capitalone.dashboard.repository.GitHubRepository;
import com.capitalone.dashboard.repository.GitRequestRepository;
import com.capitalone.dashboard.repository.WorkflowRepository;

/**
 * CollectorTask that fetches Commit information from GitHub
 */
@Component
public class GitHubCollectorTask extends CollectorTask<Collector> {
    private static final Log LOG = LogFactory.getLog(GitHubCollectorTask.class);

    private final BaseCollectorRepository<Collector> collectorRepository;
    private final GitHubRepository gitHubRepoRepository;
    private final CommitRepository commitRepository;
    private final GitRequestRepository gitRequestRepository;
    private final GitHubWorkflowClient gitHubWorkflowClient;
    private final GitHubSettings gitHubSettings;
    private final ComponentRepository dbComponentRepository;
    private static final long FOURTEEN_DAYS_MILLISECONDS = 14 * 24 * 60 * 60 * 1000;
    private static final String API_RATE_LIMIT_MESSAGE = "API rate limit exceeded";
    private List<Pattern> commitExclusionPatterns = new ArrayList<>();
    private WorkflowRepository<Workflow> workflowRepository;

    Predicate<Workflow> checkWorkFlowExist = workflow -> {return workflowRepository.exists(workflow.getWorkflowId());};
    
    @Autowired
    public GitHubCollectorTask(TaskScheduler taskScheduler,
                               BaseCollectorRepository<Collector> collectorRepository,
                               GitHubRepository gitHubRepoRepository,
                               CommitRepository commitRepository,
                               GitRequestRepository gitRequestRepository,
                               GitHubWorkflowClient gitHubWorkflowClient,
                               GitHubSettings gitHubSettings,
                               ComponentRepository dbComponentRepository,
                               WorkflowRepository<Workflow> workflowRepository) {
        super(taskScheduler, "GitHub");
        this.collectorRepository = collectorRepository;
        this.gitHubRepoRepository = gitHubRepoRepository;
        this.commitRepository = commitRepository;
        this.gitHubWorkflowClient = gitHubWorkflowClient;
        this.gitHubSettings = gitHubSettings;
        this.dbComponentRepository = dbComponentRepository;
        this.gitRequestRepository = gitRequestRepository;
        this.workflowRepository = workflowRepository;
        if (!CollectionUtils.isEmpty(gitHubSettings.getNotBuiltCommits())) {
            for (String regExStr : gitHubSettings.getNotBuiltCommits()) {
                Pattern pattern = Pattern.compile(regExStr, Pattern.CASE_INSENSITIVE);
                commitExclusionPatterns.add(pattern);
            }
        }
    }

    @Override
    public Collector getCollector() {
        Collector protoType = new Collector();
        protoType.setName("GitHub");
        protoType.setCollectorType(CollectorType.GITWORKFLOW);
        protoType.setOnline(true);
        protoType.setEnabled(true);

        Map<String, Object> allOptions = new HashMap<>();
        allOptions.put(GitHub.REPO_URL, "");
        allOptions.put(GitHub.BRANCH, "");
        allOptions.put(GitHub.USER_ID, "");
        allOptions.put(GitHub.PASSWORD, "");
        allOptions.put(GitHub.PERSONAL_ACCESS_TOKEN, "");
        protoType.setAllFields(allOptions);

        Map<String, Object> uniqueOptions = new HashMap<>();
        uniqueOptions.put(GitHub.REPO_URL, "");
        uniqueOptions.put(GitHub.BRANCH, "");
        protoType.setUniqueFields(uniqueOptions);
        return protoType;
    }

    @Override
    public BaseCollectorRepository<Collector> getCollectorRepository() {
        return collectorRepository;
    }

    @Override
    public String getCron() {
        return gitHubSettings.getCron();
    }

    /**
     * Clean up unused deployment collector items
     *
     * @param collector the {@link Collector}
     */
    @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts") // agreed, fixme
    private void clean(Collector collector) {
        Set<ObjectId> uniqueIDs = new HashSet<>();
        /**
         * Logic: For each component, retrieve the collector item list of the type SCM.
         * Store their IDs in a unique set ONLY if their collector IDs match with GitHub collectors ID.
         */
        for (com.capitalone.dashboard.model.Component comp : dbComponentRepository.findAll()) {
            if (comp.getCollectorItems() != null && !comp.getCollectorItems().isEmpty()) {
                List<CollectorItem> itemList = comp.getCollectorItems().get(CollectorType.SCM);
                if (itemList != null) {
                    for (CollectorItem ci : itemList) {
                        if (ci != null && ci.getCollectorId().equals(collector.getId())) {
                            uniqueIDs.add(ci.getId());
                        }
                    }
                }
            }
        }

        /**
         * Logic: Get all the collector items from the collector_item collection for this collector.
         * If their id is in the unique set (above), keep them enabled; else, disable them.
         */
        List<GitHub> repoList = new ArrayList<>();
        Set<ObjectId> gitID = new HashSet<>();
        gitID.add(collector.getId());
        for (GitHub repo : gitHubRepoRepository.findByCollectorIdIn(gitID)) {
            if (repo.isPushed()) {continue;}

            repo.setEnabled(uniqueIDs.contains(repo.getId()));
            repoList.add(repo);
        }
        gitHubRepoRepository.save(repoList);
    }

    @Override
    public void collect(Collector collector) {

        logBanner("Starting...");
        long start = System.currentTimeMillis();
        int repoCount = 0;
        int commitCount = 0;
        int pullCount = 0;
        int issueCount = 0;
       
        //clean(collector);
        
        String proxyUrl = gitHubSettings.getProxy();
        String proxyPort = gitHubSettings.getProxyPort();
        String proxyUser= gitHubSettings.getProxyUser();
        String proxyPassword= gitHubSettings.getProxyPassword();
        
        if (!StringUtils.isEmpty(proxyUrl) && !StringUtils.isEmpty(proxyPort)) {
            System.setProperty("http.proxyHost", proxyUrl);
            System.setProperty("https.proxyHost", proxyUrl);
            System.setProperty("http.proxyPort", proxyPort);
            System.setProperty("https.proxyPort", proxyPort);
        
         if (!StringUtils.isEmpty(proxyUser) && !StringUtils.isEmpty(proxyPassword)) {
            System.setProperty("http.proxyUser", proxyUser);
            System.setProperty("https.proxyUser", proxyUser);
            System.setProperty("http.proxyPassword", proxyPassword);
            System.setProperty("https.proxyPassword", proxyPassword);
         }
         }
            
         for (GitHub repo : enabledRepos(collector)) {
            if (repo.getErrorCount() < gitHubSettings.getErrorThreshold()) {
                boolean firstRun = ((repo.getLastUpdated() == 0) || ((start - repo.getLastUpdated()) > FOURTEEN_DAYS_MILLISECONDS));
                repo.removeLastUpdateDate();  //moved last update date to collector item. This is to clean old data.
                try {
                    LOG.info(repo.getOptions().toString() + "::" + repo.getBranch() + ":: get workflows");
                    
                    // Step 1: Get all the Workflows and save if not exist
                    gitHubWorkflowClient.getWorkflows(repo, commitExclusionPatterns).parallelStream().filter(checkWorkFlowExist).
                    forEach(workflow -> {
                    	workflowRepository.save(workflow);
                    });
                  
                    
                    
                    
                    

                 
                } catch (HttpStatusCodeException hc) {
                    LOG.error("Error fetching commits for:" + repo.getRepoUrl(), hc);
                    if (! (isRateLimitError(hc) || hc.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) ) {
                        CollectionError error = new CollectionError(hc.getStatusCode().toString(), hc.getMessage());
                        repo.getErrors().add(error);
                    }
                } catch (ResourceAccessException ex) {
                    //handle case where repo is valid but github returns connection refused due to outages??
                    if (ex.getMessage() != null && ex.getMessage().contains("Connection refused")) {
                        LOG.error("Error fetching commits for:" + repo.getRepoUrl(), ex);
                    } else {
                        LOG.error("Error fetching commits for:" + repo.getRepoUrl(), ex);
                        CollectionError error = new CollectionError(CollectionError.UNKNOWN_HOST, repo.getRepoUrl());
                        repo.getErrors().add(error);
                    }
                } catch (RestClientException | MalformedURLException ex) {
                    LOG.error("Error fetching commits for:" + repo.getRepoUrl(), ex);
                    CollectionError error = new CollectionError(CollectionError.UNKNOWN_HOST, repo.getRepoUrl());
                    repo.getErrors().add(error);
                } catch (HygieiaException he) {
                    LOG.error("Error fetching commits for:" + repo.getRepoUrl(), he);
                    CollectionError error = new CollectionError("Bad repo url", repo.getRepoUrl());
                    repo.getErrors().add(error);
                }
                gitHubRepoRepository.save(repo);
            }
            repoCount++;
        }
        log("New Commits", start, commitCount);

        log("Finished", start);
    }


    private int processList(GitHub repo, List<GitRequest> entries, String type) {
        int count = 0;
        if (CollectionUtils.isEmpty(entries)) return 0;

        for (GitRequest entry : entries) {
            LOG.debug(entry.getTimestamp() + ":::" + entry.getScmCommitLog());
            GitRequest existing = gitRequestRepository.findByCollectorItemIdAndNumberAndRequestType(repo.getId(), entry.getNumber(), type);

            if (existing == null) {
                entry.setCollectorItemId(repo.getId());
                count++;
            } else {
                entry.setId(existing.getId());
                entry.setCollectorItemId(repo.getId());
            }
            gitRequestRepository.save(entry);

            //fix merge commit type for squash merged and rebased merged PRs
            //PRs that were squash merged or rebase merged have only one parent
            if ("pull".equalsIgnoreCase(type) && "merged".equalsIgnoreCase(entry.getState())) {
                List<Commit> commits = commitRepository.findByScmRevisionNumber(entry.getScmRevisionNumber());
                for(Commit commit : commits) {
                    if (commit.getType() != null) {
                        if (commit.getType() != CommitType.Merge) {
                            commit.setType(CommitType.Merge);
                            commitRepository.save(commit);
                        }
                    } else {
                        commit.setType(CommitType.Merge);
                        commitRepository.save(commit);
                    }
                }
            }
        }
        return count;
    }

    private boolean isRateLimitError(HttpStatusCodeException hc) {
        String response = hc.getResponseBodyAsString();
        return StringUtils.isEmpty(response) ? false : response.contains(API_RATE_LIMIT_MESSAGE);
    }

    private List<GitHub> enabledRepos(Collector collector) {
        List<GitHub> repos = (List<GitHub>) gitHubRepoRepository.findEnabledGitHubRepos(collector.getId());

        List<GitHub> pulledRepos 
                =  (List<GitHub>) Optional.ofNullable(repos)
                .orElseGet(Collections::emptyList).stream()
                .filter(pulledRepo -> !pulledRepo.isPushed())
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(pulledRepos)) { return new ArrayList<>(); }

        return pulledRepos;
    }

    
    private boolean isNewCommit(GitHub repo, Commit commit) {
        return commitRepository.findByCollectorItemIdAndScmRevisionNumber(
                repo.getId(), commit.getScmRevisionNumber()) == null;
    }

    private GitRequest getExistingRequest(GitHub repo, GitRequest request, String type) {
        return gitRequestRepository.findByCollectorItemIdAndNumberAndRequestType(
                repo.getId(), request.getNumber(), type);
    }
}
