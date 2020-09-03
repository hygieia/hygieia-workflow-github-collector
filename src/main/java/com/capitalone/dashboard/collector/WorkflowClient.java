package com.capitalone.dashboard.collector;

import java.net.MalformedURLException;
import java.util.List;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.GitWorkflowRepo;
import com.capitalone.dashboard.model.GitWorkflowRepo;
import com.capitalone.dashboard.model.GitWorkflow;
import com.capitalone.dashboard.model.GitWorkflowRun;
import com.capitalone.dashboard.model.GitWorkflowRunJob;

/**
 * Client for fetching workflow history from GitWorkflow
 */
public interface WorkflowClient {

	List<GitWorkflow> getGitWorkflows(GitWorkflowRepo repo) throws MalformedURLException, HygieiaException;

	List<GitWorkflowRun> getGitWorkflowRuns(GitWorkflowRepo repo, String workflowId) throws MalformedURLException, HygieiaException;

	List<GitWorkflowRunJob> getGitWorkflowRunJobs(GitWorkflowRepo repo, String workflowId, String workflowRunId) throws MalformedURLException, HygieiaException;
	
}
