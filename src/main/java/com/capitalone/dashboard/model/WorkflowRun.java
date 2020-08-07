package com.capitalone.dashboard.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/*Model to store a GitHub Workflow Run Data*/

@Document(collection="github_workflow_run")
public class WorkflowRun extends BaseModel{
	@Indexed(unique = true)
	private String workflowId;
	private String runId;
	private String event;
	private String status;
	private String conclusion;
	private String created_at;
	private String updated_at;
	
	public WorkflowRun(String workflowId, String runId, String status, String conclusion) {
		this.workflowId = workflowId;
		this.runId = runId;
		this.status = status;
		this.conclusion = conclusion;
	}
	
	public WorkflowRun(String workflowId, String runId, String status, String conclusion,
			String event, String created_at, String updated_at) {
		this.workflowId = workflowId;
		this.runId = runId;
		this.status = status;
		this.conclusion = conclusion;
		this.event = event;
		this.created_at = created_at;
		this.updated_at = updated_at;
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

}
