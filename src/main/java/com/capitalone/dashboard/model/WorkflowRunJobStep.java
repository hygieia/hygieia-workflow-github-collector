package com.capitalone.dashboard.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/*Model to store a GitHub Workflow Run Job Step Data*/

@Document(collection="github_workflow_run_job_step")
public class WorkflowRunJobStep extends BaseModel{
	@Indexed(unique = true)
	private Long workflowId;
	private Long runId;
	private Long jobId;
	private Long stepNumber;
	private String status;
	private String conclusion;
	private String started_at;
	private String completed_at;
	private String name;
	
	public WorkflowRunJobStep(Long workflowId, Long runId, Long jobId, Long stepNumber,
			String status, String conclusion, String started_at, String completed_at,
			String name) {
		this.workflowId = workflowId;
		this.runId = runId;
		this.jobId = jobId;
		this.stepNumber = stepNumber;
		this.status = status;
		this.conclusion = conclusion;
		this.started_at = started_at;
		this.completed_at = completed_at;
		this.name = name;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Long getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Long stepNumber) {
		this.stepNumber = stepNumber;
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

	public String getStarted_at() {
		return started_at;
	}

	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}

	public String getCompleted_at() {
		return completed_at;
	}

	public void setCompleted_at(String completed_at) {
		this.completed_at = completed_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
