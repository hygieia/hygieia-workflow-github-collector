package com.capitalone.dashboard.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/*Model to store a GitHub Workflow Run Data*/

@Document(collection="github_workflow_run")
public class GitWorkflowRun extends BaseModel{
	@Indexed(unique = true)
	private String workflowId;
	private String runId;
	private String event;
	private String status;
	private String conclusion;
	private String createdAt;
	private String updatedAt;
	
	@DBRef
	private Set<GitWorkflowRunJob> runJobs = new HashSet<GitWorkflowRunJob>();
	
	public GitWorkflowRun(String workflowId, String runId, String status, String conclusion) {
		this.workflowId = workflowId;
		this.runId = runId;
		this.status = status;
		this.conclusion = conclusion;
	}
	
	public GitWorkflowRun(String workflowId, String runId, String status, String conclusion,
			String event, String createdAt, String updatedAt) {
		this.workflowId = workflowId;
		this.runId = runId;
		this.status = status;
		this.conclusion = conclusion;
		this.event = event;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	

	public GitWorkflowRun(String workflowId, String runId, String event, String status, String conclusion,
			String createdAt, String updatedAt, Set<GitWorkflowRunJob> runJobs) {
		super();
		this.workflowId = workflowId;
		this.runId = runId;
		this.event = event;
		this.status = status;
		this.conclusion = conclusion;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.runJobs = runJobs;
	}

	public Set<GitWorkflowRunJob> getRunJobs() {
		return runJobs;
	}

	public void setRunJobs(Set<GitWorkflowRunJob> runJobs) {
		this.runJobs = runJobs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}
