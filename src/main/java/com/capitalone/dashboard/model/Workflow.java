package com.capitalone.dashboard.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "github_workflow")
public class Workflow extends CollectorItem{
	@Indexed(unique = true)
	private String workflowId;
	private String name;
	private String state;
	private String created_at;
	private String updated_at;
	private Boolean enabled;
	private List<WorkflowRun> workflowRuns;
		
	public Workflow(String workflowId, String name, String state, Boolean enabled) {
		this.workflowId = workflowId;
		this.name = name;
		this.state = state;
		this.enabled =enabled;
	}
	
	public Workflow(String workflowId, String name, String state, Boolean enabled,
			String created_at, String updated_at) {
		this.workflowId = workflowId;
		this.name = name;
		this.state = state;
		this.enabled =enabled;
		this.created_at = created_at;
		this.updated_at = updated_at;
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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

	public List<WorkflowRun> getWorkflowRuns() {
		return workflowRuns;
	}

	public void setWorkflowRuns(List<WorkflowRun> workflowRuns) {
		this.workflowRuns = workflowRuns;
	}
		
}
