package com.capitalone.dashboard.model;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.capitalone.dashboard.model.webhook.github.GitHubRepo;

/**
 * CollectorItem extension to store the github repo url, branch & Workflow
 */
@Document(collection = "github_workflow")
public class GitWorkflow extends BaseModel{
    private ObjectId gitWorkFlowRepoId;
    @Indexed(unique = true)
	private String workflowId;
	private String name;
	private String state;
	private String created_at;
	private String updated_at;
	
	@DBRef
	private Set<GitWorkflowRun> runs = new HashSet<GitWorkflowRun>();
	
	public GitWorkflow() {
		// TODO Auto-generated constructor stub
	}
	
	
	public GitWorkflow(String workflowId, String name, String state, String created_at, String updated_at) {
		super();
		this.workflowId = workflowId;
		this.name = name;
		this.state = state;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
	
	
	public GitWorkflow(ObjectId gitWorkFlowRepoId, String workflowId, String name, String state, String created_at,
			String updated_at, Set<GitWorkflowRun> runs) {
		super();
		this.gitWorkFlowRepoId = gitWorkFlowRepoId;
		this.workflowId = workflowId;
		this.name = name;
		this.state = state;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.runs = runs;
	}



	public ObjectId getGitWorkFlowRepoId() {
		return gitWorkFlowRepoId;
	}


	public void setGitWorkFlowRepoId(ObjectId gitWorkFlowRepoId) {
		this.gitWorkFlowRepoId = gitWorkFlowRepoId;
	}


	public String getWorkflowId() {
		return workflowId;
	}




	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getState() {
		return state;
	}




	public void setState(String state) {
		this.state = state;
	}




	public String getCreated_at() {
		return created_at;
	}




	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}




	public String getUpdated_at() {
		return updated_at;
	}




	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}




	public Set<GitWorkflowRun> getRuns() {
		return runs;
	}




	public void setRuns(Set<GitWorkflowRun> runs) {
		this.runs = runs;
	}




	/*
	 * @Override public boolean equals(Object o) { if (this == o) { return true; }
	 * if (o == null || getClass() != o.getClass()) { return false; }
	 * 
	 * GitWorkflow gitHubRepo = (GitWorkflow) o;
	 * 
	 * return getRepoUrl().equals(gitHubRepo.getRepoUrl()) &
	 * getBranch().equals(gitHubRepo.getBranch()) &
	 * getWorkflow().equals(gitHubRepo.getWorkflow()); }
	 * 
	 * @Override public int hashCode() { return getWorkflow().hashCode(); }
	 */

}
