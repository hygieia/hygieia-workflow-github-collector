package com.capitalone.dashboard.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.model.ComponentData;
import com.capitalone.dashboard.service.WorkflowServiceImpl;

@RestController
public class WorkflowController {
	private static final Log LOG = LogFactory.getLog(WorkflowController.class);

	private final WorkflowServiceImpl workflowService;

	@Autowired
	public WorkflowController(WorkflowServiceImpl workflowService) {
		this.workflowService = workflowService;
	}

	@RequestMapping(value = "/collector/workflow/meta/count", method = GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ComponentData> getWorkflowMetaCount() {
		LOG.debug("Call Recieved @ //collector/workflow/meta/count :: ");
		return ResponseEntity.ok(workflowService.getWorkflowMetaCount());
	}
	
	@RequestMapping(value = "/collector/docker/meta/data", method = GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ComponentData> getDockerMetaData() {
		LOG.debug("Call Recieved @ /collector/docker/meta/data :: ");
		return ResponseEntity.ok(dockerService.getDockerMetaData());
	}
	
	 
	@RequestMapping(value = "/collector/docker/cpu/stats", method = GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ComponentData> getDockerCpuStats() {
		LOG.debug("Call Recieved @ /collector/docker/cpu/stats :: ");
		return ResponseEntity.ok(dockerService.getDockerCpuStats());
	}

	
}
