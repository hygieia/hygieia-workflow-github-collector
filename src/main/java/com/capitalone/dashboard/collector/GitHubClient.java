package com.capitalone.dashboard.collector;

import java.net.MalformedURLException;
import java.util.List;
import java.util.regex.Pattern;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitWorkflowRepo;

/**
 * Client for fetching commit history from GitHub
 */
public interface GitHubClient {


	List<Commit> getCommits(GitWorkflowRepo repo, boolean firstRun, List<Pattern> commitExclusionPatterns) throws MalformedURLException, HygieiaException;

	List<GitWorkflowRepo> getWorkflow(GitWorkflowRepo repo, boolean firstRun, List<Pattern> commitExclusionPatterns) throws MalformedURLException, HygieiaException;
	
   
}
