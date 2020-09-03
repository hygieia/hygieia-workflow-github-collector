package com.capitalone.dashboard.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capitalone.dashboard.collector.GitWorkflowSettings;
import com.capitalone.dashboard.collector.WorkflowClient;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.ComponentData;
import com.capitalone.dashboard.model.GitWorkflow;
import com.capitalone.dashboard.model.GitWorkflowRepo;
import com.capitalone.dashboard.model.GitWorkflowRun;
import com.capitalone.dashboard.model.GitWorkflowRunJob;
import com.capitalone.dashboard.model.Series;
import com.capitalone.dashboard.repository.GitWorkflowRepoRepository;
import com.capitalone.dashboard.repository.WorkflowCustomRepository;
import com.capitalone.dashboard.repository.WorkflowRunJobRepository;
import com.capitalone.dashboard.repository.WorkflowRunRepository;

@Service
public class WorkflowServiceImpl implements WorkflowService {
	private static final Log LOG = LogFactory.getLog(WorkflowService.class);

	@Autowired
	GitWorkflowRepoRepository<GitWorkflowRepo> workflowRepoRepository;
	
	@Autowired
	WorkflowRunRepository<GitWorkflowRun> workflowRunRepository;
	
	@Autowired
	WorkflowRunJobRepository<GitWorkflowRunJob> workflowRunJobRepository;
	
	@Autowired
	WorkflowCustomRepository workflowCustomRepository;

	@Autowired
	GitWorkflowSettings gitHubSettings;

	@Autowired
	WorkflowClient workflowClient; 
	private boolean isConfigSet() {
		// return collectorItemRepository.findAll().iterator().hasNext();
		return false;
	}

	@Override
	public List<JSONObject> getWorkflowMetaCount(String workflowId) {
		// TODO Auto-generated method stub
		
		 List<JSONObject> result = new ArrayList<JSONObject>();
		 
		 JSONObject runObj = new JSONObject();
		 
		// workflowRunRepository.
		
		
		
		return null;
	}

	@Override
	public List<GitWorkflow> testAndFetchGitWorkflows(String url, String branch, String userId, String password,
			String personalAccessToken) throws MalformedURLException, HygieiaException{
		// TODO Auto-generated method stub
		List<GitWorkflow> workflows = null;
		GitWorkflowRepo gitHub = new GitWorkflowRepo();
			gitHub.setRepoUrl(url);
			gitHub.setBranch(branch);
			gitHub.setUserId(userId);
			gitHub.setPassword(password);
			gitHub.setPersonalAccessToken(personalAccessToken);
		
			workflows = workflowClient.getGitWorkflows(gitHub);
		
		
		return workflows;
	}

}
