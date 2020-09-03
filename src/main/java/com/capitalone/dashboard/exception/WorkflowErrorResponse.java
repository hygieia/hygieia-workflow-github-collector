package com.capitalone.dashboard.exception;

public class WorkflowErrorResponse {
	private int status;
	
	private String message;
	
	public WorkflowErrorResponse() {
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
