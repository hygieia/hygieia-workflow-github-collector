package com.capitalone.dashboard.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "github_workflow")
public class Workflow extends CollectorItem{
	@Indexed(unique = true)
	private String workflowId;
	private String name;
	private String state;
	private Boolean enabled;
		
	public Workflow(String workflowId, String name, String state, Boolean enabled) {
		this.workflowId = workflowId;
		this.name = name;
		this.state = state;
		this.enabled =enabled;
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
	
	
	
}
