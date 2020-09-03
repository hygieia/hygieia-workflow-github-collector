package com.capitalone.dashboard.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capitalone.dashboard.exception.WorkflowErrorResponse;
import com.capitalone.dashboard.exception.WorkflowException;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.GitWorkflow;
import com.capitalone.dashboard.service.WorkflowServiceImpl;

@RestController
public class WorkflowController {
	private static final Log LOG = LogFactory.getLog(WorkflowController.class);

	private final WorkflowServiceImpl workflowService;

	@Autowired
	public WorkflowController(WorkflowServiceImpl workflowService) {
		this.workflowService = workflowService;
	}

	@RequestMapping(value = "/workflow/meta/count", method = GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<JSONObject>> getWorkflowMetaCount(@RequestParam("workflowId") String workflowId) {
		LOG.debug("Call Recieved @ /collector/workflows/meta/count :: ");
		return ResponseEntity.ok(workflowService.getWorkflowMetaCount(workflowId));
	}

	

	@RequestMapping(value = "/workflow/test", method = GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<GitWorkflow>> testWorkFlowsByRepo(@RequestParam("url") String url,
			@RequestParam("branch") String branch, @RequestParam("userID") String userID,
			@RequestParam("password") String password,
			@RequestParam("personalAccessToken") String personalAccessToken) throws WorkflowException, HygieiaException, MalformedURLException {
		
		return ResponseEntity.ok(workflowService.testAndFetchGitWorkflows(url,branch, userID, password,personalAccessToken));

	}

	
	// Exception Handler
	@ExceptionHandler
	public ResponseEntity<WorkflowErrorResponse> handleWorkflowException(WorkflowException exception){
		WorkflowErrorResponse workflowErrorResponse = new WorkflowErrorResponse();
		workflowErrorResponse.setStatus(org.apache.http.HttpStatus.SC_ACCEPTED);
		workflowErrorResponse.setMessage(exception.getMessage());
		
		return new ResponseEntity<WorkflowErrorResponse>(workflowErrorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<WorkflowErrorResponse> handleHygieiaException(HygieiaException exception){
		WorkflowErrorResponse workflowErrorResponse = new WorkflowErrorResponse();
		workflowErrorResponse.setStatus(org.apache.http.HttpStatus.SC_ACCEPTED);
		workflowErrorResponse.setMessage(exception.getMessage());
		
		return new ResponseEntity<WorkflowErrorResponse>(workflowErrorResponse, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler
	public ResponseEntity<WorkflowErrorResponse> handleHygieiaException(MalformedURLException exception){
		WorkflowErrorResponse workflowErrorResponse = new WorkflowErrorResponse();
		workflowErrorResponse.setStatus(org.apache.http.HttpStatus.SC_ACCEPTED);
		workflowErrorResponse.setMessage(exception.getMessage());
		
		return new ResponseEntity<WorkflowErrorResponse>(workflowErrorResponse, HttpStatus.NOT_FOUND);
	}
	
}
