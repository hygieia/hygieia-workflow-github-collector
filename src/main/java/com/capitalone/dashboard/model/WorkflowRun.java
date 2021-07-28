package com.capitalone.dashboard.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/*Model to store a GitHub Workflow Run Data*/

@Document(collection="github_workflow_run")
public class WorkflowRun extends BaseModel{
	@Indexed(unique = true)
	private String runId;
	private String workflowId;
	private String event;
	private String status;
	private String conclusion;
	private String createdAt;
	private String updatedAt;
	private String buildUrl;
	private List<SCM> sourceChangeSet;
	public WorkflowRun()
	{
		
	}
	public WorkflowRun(String workflowId, String runId, String status, String conclusion) {
		this.workflowId = workflowId;
		this.runId = runId;
		this.status = status;
		this.conclusion = conclusion;
	}
	
	public WorkflowRun(String workflowId, String runId, String status, String conclusion,
			String event, String createdAt, String updatedAt,String buildUrl,List<SCM> sourceChangeSet) {
		this.workflowId = workflowId;
		this.runId = runId;
		this.status = status;
		this.conclusion = conclusion;
		this.event = event;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.buildUrl = buildUrl;
		this.sourceChangeSet = sourceChangeSet;
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
	public String getBuildUrl() {
		return buildUrl;
	}
	public void setBuildUrl(String buildUrl) {
		this.buildUrl = buildUrl;
	}
	public List<SCM> getSourceChangeSet() {
		return sourceChangeSet;
	}
	public void setSourceChangeSet(List<SCM> sourceChangeSet) {
		this.sourceChangeSet = sourceChangeSet;
	}

}
