package com.capitalone.dashboard.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "github_workflow")
public class Workflow {
	@Indexed(unique = true)
	private String workflowId;
	private String name;
	private String state;
	
	public Workflow(String workflowId, String name, String state) {
		this.workflowId = workflowId;
		this.name = name;
		this.state = state;
	}
}
