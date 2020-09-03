package com.capitalone.dashboard.model;

	import java.util.List;

import org.bson.types.ObjectId;
	import org.springframework.data.mongodb.core.index.Indexed;
	import org.springframework.data.mongodb.core.mapping.Document;

	/*Model to store a GitHub Workflow Run Job Data*/

	@Document(collection="github_workflow_run_job")
	public class GitWorkflowRunJob extends BaseModel{
		@Indexed(unique = true)
		private String workflowId;
		private String runId;
		private String jobId;
		private String status;
		private String conclusion;
		private String started_at;
		private String completed_at; 
		private String name; 
		private List<GitWorkflowRunJobStep> workflowRunJobSteps;
		
		public GitWorkflowRunJob(String workflowId, String runId, String jobId,
				String status, String conclusion, String started_at, String completed_at,
				String name) {
			this.workflowId = workflowId;
			this.runId = runId;
			this.jobId = jobId;
			this.status = status;
			this.conclusion = conclusion;
			this.started_at = started_at;
			this.completed_at = completed_at;
			this.name = name;
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

		public String getJobId() {
			return jobId;
		}

		public void setJobId(String jobId) {
			this.jobId = jobId;
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

		public List<GitWorkflowRunJobStep> getWorkflowRunJobSteps() {
			return workflowRunJobSteps;
		}

		public void setWorkflowRunJobSteps(List<GitWorkflowRunJobStep> workflowRunJobSteps) {
			this.workflowRunJobSteps = workflowRunJobSteps;
		}

}
