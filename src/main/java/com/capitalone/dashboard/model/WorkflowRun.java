package com.capitalone.dashboard.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/*Model to store a GitHub Workflow Run Data*/

@Document(collection="github_workflow_run")
public class WorkflowRun extends BaseModel{
	@Indexed(unique = true)
	private String workflowId;
	private String runId;
	private String status;
	private String conclusion;
	
	public WorkflowRun(String workflowId, String runId, String status, String conclusion) {
		this.workflowId = workflowId;
		this.runId = runId;
		this.status = status;
		this.conclusion = conclusion;
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

}
