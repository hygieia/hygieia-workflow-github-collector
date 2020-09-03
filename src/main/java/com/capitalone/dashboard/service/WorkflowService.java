package com.capitalone.dashboard.service;

import java.net.MalformedURLException;
import java.util.List;

import org.json.simple.JSONObject;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.GitWorkflow;

public interface WorkflowService {
	
	 List<JSONObject> getWorkflowMetaCount(String workflowId);
	 
	 List<GitWorkflow> testAndFetchGitWorkflows(String url, String branch, String userId, String password,
				String personalAccessToken) throws MalformedURLException, HygieiaException;

}
