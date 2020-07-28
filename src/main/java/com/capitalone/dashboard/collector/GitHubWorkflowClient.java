package com.capitalone.dashboard.collector;

import java.net.MalformedURLException;
import java.util.List;
import java.util.regex.Pattern;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.GitHub;
import com.capitalone.dashboard.model.WorkflowRun;
import com.capitalone.dashboard.model.WorkflowRunJob;
import com.capitalone.dashboard.model.WorkflowRunJobStep;

/**
 * Client for fetching workflow history from GitHub
 */
public interface GitHubWorkflowClient {

	List<WorkflowRun> getWorkflowRuns(GitHub repo, boolean firstRun, List<Pattern> exclusionPatterns) throws MalformedURLException, HygieiaException;

	List<WorkflowRunJob> getWorkflowRunJobs(List<WorkflowRun> runs, boolean firstRun, List<Pattern> exclusionPatterns) throws MalformedURLException, HygieiaException;

	List<WorkflowRunJobStep> getWorkflowRunJobSteps(List<WorkflowRunJob> jobs, boolean firstRun, List<Pattern> exclusionPatterns) throws MalformedURLException, HygieiaException;

}
