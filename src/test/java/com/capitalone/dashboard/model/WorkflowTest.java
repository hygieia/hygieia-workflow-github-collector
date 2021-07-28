package com.capitalone.dashboard.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class WorkflowTest {
	
	private String workflowId = "8675309";
	private String name = "wfName";
	private String state = "active";
	private Boolean enabled = true;
	private String createdAt = "17/08/1956 12:01";
	private String updatedAt = "17/08/2020 12:01";
	private String repo="repo";
	private String branch = "master";
	private String workflowId2 = "8675310";
	private String name2 = "wfName2";
	private String state2 = "passive";
	private Boolean enabled2 = false;
	private String createdAt2 = "14/08/2020 12:01";
	private String updatedAt2 = "14/08/2020 14:01";
	private String repo2="repo";
	private String branch2 = "main";
	@Test
	public void testConstructor1() {
		Workflow wf = new Workflow(workflowId, name, state, enabled,createdAt, updatedAt,repo,branch);
		assertEquals(workflowId,wf.getWorkflowId());
		assertEquals(name,wf.getName());
		assertEquals(state,wf.getState());
		assertEquals(enabled,wf.getEnabled());
		assertNotNull(wf.getCreatedAt());
		assertNotNull(wf.getUpdatedAt());
	}

	@Test
	public void testConstructor2WithSetters() {
		Workflow wf = new Workflow(workflowId, name, state, enabled, createdAt, updatedAt,repo,branch);
		assertEquals(workflowId,wf.getWorkflowId());
		assertEquals(name,wf.getName());
		assertEquals(state,wf.getState());
		assertEquals(enabled,wf.getEnabled());
		assertEquals(createdAt,wf.getCreatedAt());
		assertEquals(updatedAt,wf.getUpdatedAt());
		
		wf.setWorkflowId(workflowId2);
		wf.setName(name2);
		wf.setState(state2);
		wf.setEnabled(enabled2);
		wf.setCreatedAt(createdAt2);
		wf.setUpdatedAt(updatedAt2);
		assertEquals(workflowId2,wf.getWorkflowId());
		assertEquals(name2,wf.getName());
		assertEquals(state2,wf.getState());
		assertEquals(enabled2,wf.getEnabled());
		assertEquals(createdAt2,wf.getCreatedAt());
		assertEquals(updatedAt2,wf.getUpdatedAt());
	}

}
