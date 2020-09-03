package com.capitalone.dashboard.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.capitalone.dashboard.model.webhook.github.GitHubRepo;

/**
 * CollectorItem extension to store the github repo url, branch & Workflow
 */
@Document(collection = "github_workflow_repo")
public class GitWorkflowRepo extends GitHubRepo{
    
    public static final String WORKFLOW = "workflow";
	
	public GitWorkflowRepo() {
		// TODO Auto-generated constructor stub
	}
	
	public String getWorkflow() {
		return (String) getOptions().get(this.WORKFLOW); 
	}
    
	
    

	/*
	 * public boolean equals(Object o) { if (this == o) { return true; } if (o ==
	 * null || getClass() != o.getClass()) { return false; }
	 * 
	 * GitWorkflowRepo gitHubRepo = (GitWorkflowRepo) o;
	 * 
	 * return getRepoUrl().equals(gitHubRepo.getRepoUrl()) &
	 * getBranch().equals(gitHubRepo.getBranch()) &
	 * getWorkflow().equals(gitHubRepo.getWorkflow()); }
	 * 
	 * @Override public int hashCode() { return getWorkflow().hashCode(); }
	 * 
	 */}
