package com.capitalone.dashboard.collector;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.GitWorkflow;
import com.capitalone.dashboard.model.GitWorkflowRepo;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.ComponentRepository;
import com.capitalone.dashboard.repository.GitWorkflowRepoRepository;
import com.capitalone.dashboard.repository.GitWorkflowRepository;
import com.capitalone.dashboard.repository.WorkflowRunJobRepository;
import com.capitalone.dashboard.repository.WorkflowRunRepository;

/**
 * CollectorTask that fetches Commit information from GitWorkflow
 */
@Component
public class WorkflowCollectorTask extends CollectorTask<Collector> {
	private static final Log LOG = LogFactory.getLog(WorkflowCollectorTask.class);

	private final BaseCollectorRepository<Collector> collectorRepository;
	private final WorkflowClient workflowClient;
	private final GitWorkflowSettings GitWorkflowSettings;
	private final ComponentRepository dbComponentRepository;
	private static final String API_RATE_LIMIT_MESSAGE = "API rate limit exceeded";
	private final GitWorkflowRepoRepository<GitWorkflowRepo> workflowRepoRepository;
	private final GitWorkflowRepository<GitWorkflow> workflowRepository;
	private final WorkflowRunRepository workflowRunRepository;
	private final WorkflowRunJobRepository workflowRunJobRepository;

	//private final Predicate<GitWorkflowRepo> checkWorkFlowExist;

	private final String ACTIVE = "active";
	private final Predicate<GitWorkflow> isWorkFlowActive;
	
	@Autowired
	public WorkflowCollectorTask(TaskScheduler taskScheduler, BaseCollectorRepository<Collector> collectorRepository,
			GitWorkflowRepoRepository workflowRepoRepository, GitWorkflowRepository workflowRepository, WorkflowClient workflowClient,
			GitWorkflowSettings GitWorkflowSettings, ComponentRepository dbComponentRepository,
			WorkflowRunRepository workflowRunRepository,
			WorkflowRunJobRepository workflowRunJobRepository) {
		super(taskScheduler, "GitWorkflow");
		this.collectorRepository = collectorRepository;
		this.workflowRepository = workflowRepository;
		this.workflowClient = workflowClient;
		this.GitWorkflowSettings = GitWorkflowSettings;
		this.dbComponentRepository = dbComponentRepository;
		this.workflowRepoRepository = workflowRepoRepository;
		this.workflowRunRepository = workflowRunRepository;
		this.workflowRunJobRepository = workflowRunJobRepository;

		/*
		 * this.checkWorkFlowExist = workflow -> { return
		 * workflowRepoRepository.exists(workflow.getWorkflowId()); };
		 */
		
		this.isWorkFlowActive = workflow -> {
			return workflow.getState().equalsIgnoreCase(ACTIVE);
		};
	}

	@Override
	public Collector getCollector() {
		Collector protoType = new Collector();
		protoType.setName("GitWorkflow"); // the name has to same
		protoType.setCollectorType(CollectorType.GitWorkflow);
		protoType.setOnline(true);
		protoType.setEnabled(true);

		Map<String, Object> allOptions = new HashMap<>();
		allOptions.put(GitWorkflowRepo.REPO_URL, "");
		allOptions.put(GitWorkflowRepo.BRANCH, "");
		allOptions.put(GitWorkflowRepo.USER_ID, "");
		allOptions.put(GitWorkflowRepo.PASSWORD, "");
		allOptions.put(GitWorkflowRepo.PERSONAL_ACCESS_TOKEN, "");
		allOptions.put(GitWorkflowRepo.WORKFLOW, "");
		protoType.setAllFields(allOptions);

		Map<String, Object> uniqueOptions = new HashMap<>();
		uniqueOptions.put(GitWorkflowRepo.REPO_URL, "");
		uniqueOptions.put(GitWorkflowRepo.BRANCH, "");
		uniqueOptions.put(GitWorkflowRepo.WORKFLOW, "");
		protoType.setUniqueFields(uniqueOptions);
		return protoType;
	}

	@Override
	public BaseCollectorRepository<Collector> getCollectorRepository() {
		return collectorRepository;
	}

	@Override
	public String getCron() {
		return GitWorkflowSettings.getCron();
	}

	/**
	 * Clean up unused deployment collector items
	 *
	 * @param collector the {@link Collector}
	 */
	/*
	 * @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts") // agreed, fixme private
	 * void clean(Collector collector) { Set<ObjectId> uniqueIDs = new HashSet<>();
	 *//**
		 * Logic: For each component, retrieve the collector item list of the type SCM.
		 * Store their IDs in a unique set ONLY if their collector IDs match with
		 * GitWorkflow collectors ID.
		 */

	/*
	 * for (com.capitalone.dashboard.model.Component comp :
	 * dbComponentRepository.findAll()) { if (comp.getCollectorItems() != null &&
	 * !comp.getCollectorItems().isEmpty()) { List<CollectorItem> itemList =
	 * comp.getCollectorItems().get(CollectorType.SCM); if (itemList != null) { for
	 * (CollectorItem ci : itemList) { if (ci != null &&
	 * ci.getCollectorId().equals(collector.getId())) { uniqueIDs.add(ci.getId()); }
	 * } } } }
	 * 
	 *//**
		 * Logic: Get all the collector items from the collector_item collection for
		 * this collector. If their id is in the unique set (above), keep them enabled;
		 * else, disable them.
		 *//*
			 * List<GitWorkflow> repoList = new ArrayList<>(); Set<ObjectId> gitID = new
			 * HashSet<>(); gitID.add(collector.getId()); for (GitWorkflow repo :
			 * workflowRepository.findByCollectorIdIn(gitID)) { if (repo.isPushed())
			 * {continue;}
			 * 
			 * repo.setEnabled(uniqueIDs.contains(repo.getId())); repoList.add(repo); }
			 * GitWorkflowRepository.save(repoList); }
			 */

	@Override
	public void collect(Collector collector) {

		logBanner("Starting...");
		long start = System.currentTimeMillis();
		int repoCount = 0;

		// clean(collector);

		String proxyUrl = GitWorkflowSettings.getProxy();
		String proxyPort = GitWorkflowSettings.getProxyPort();
		String proxyUser = GitWorkflowSettings.getProxyUser();
		String proxyPassword = GitWorkflowSettings.getProxyPassword();

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

		for (GitWorkflowRepo repo : enabledRepos(collector)) {
			if (repo.getErrorCount() < GitWorkflowSettings.getErrorThreshold()) {
					LOG.info(repo.getOptions().toString() + "::" + repo.getBranch() + ":: get workflows");
					// Step 1: Get all the Workflows and save if not exist
					
					
					try {
						getWorkflowDetailsDeep(repo);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (HygieiaException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				
			}
			repoCount++;
		}
		log("New Repos", start, repoCount);

		log("Finished", start);
	}
	
	
	
	  private List<GitWorkflow> getWorkflowDetailsDeep(GitWorkflowRepo repo) throws  MalformedURLException, HygieiaException{
		  List<GitWorkflow> workflows = new ArrayList<GitWorkflow>();
		  workflowClient.getGitWorkflows(repo).parallelStream().filter(isWorkFlowActive).forEach(workflow -> {
				
				try {
					workflowClient.getGitWorkflowRuns(repo, workflow.getWorkflowId()).forEach(run -> {
						workflow.getRuns().add(run);
						
						try {
							workflowClient.getGitWorkflowRunJobs(repo, workflow.getWorkflowId(), run.getRunId()).forEach(job -> {
								run.getRunJobs().add(job);
							});
						} catch (MalformedURLException | HygieiaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
				} catch (MalformedURLException | HygieiaException | HttpClientErrorException e) {
					// TODO Auto-generated catch block
					//if(e.getCause())
					e.printStackTrace();
				}
				workflow.setGitWorkFlowRepoId(repo.getId());
			    workflowRepository.save(workflow);
				workflows.add(workflow);
				
			
			});
		  
		  return workflows;
	  }
	 

	private boolean isRateLimitError(HttpStatusCodeException hc) {
		String response = hc.getResponseBodyAsString();
		return StringUtils.isEmpty(response) ? false : response.contains(API_RATE_LIMIT_MESSAGE);
	}

	private List<GitWorkflowRepo> enabledRepos(Collector collector) {
		System.out.println(collector);
		  List<GitWorkflowRepo> repos = (List<GitWorkflowRepo>) workflowRepoRepository
				.findByCollectorId(collector.getId());

		List<GitWorkflowRepo> pulledRepos = (List<GitWorkflowRepo>) Optional.ofNullable(repos).orElseGet(Collections::emptyList)
				.stream().filter(pulledRepo -> !pulledRepo.isPushed()).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(pulledRepos)) {
			return new ArrayList<>();
		}

		return pulledRepos;
	}
}
