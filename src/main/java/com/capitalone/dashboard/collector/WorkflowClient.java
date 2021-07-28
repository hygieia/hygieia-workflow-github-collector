package com.capitalone.dashboard.collector;

import java.net.MalformedURLException;
import java.util.List;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.GitHub;
import com.capitalone.dashboard.model.Workflow;
import com.capitalone.dashboard.model.WorkflowRun;
import com.capitalone.dashboard.model.WorkflowRunJob;

/**
 * Client for fetching workflow history from GitHub
 */
public interface WorkflowClient {

	List<Workflow> getWorkflows(GitHub repo) throws MalformedURLException, HygieiaException;

	List<WorkflowRun> getWorkflowRuns(GitHub repo, String workflowId) throws MalformedURLException, HygieiaException;

	List<WorkflowRunJob> getWorkflowRunJobs(GitHub repo, String workflowId, String workflowRunId) throws MalformedURLException, HygieiaException;

}
