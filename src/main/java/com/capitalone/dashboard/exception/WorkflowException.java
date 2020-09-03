package com.capitalone.dashboard.exception;

public class WorkflowException extends RuntimeException {
	
	public WorkflowException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WorkflowException(String message) {
		super(message);
	}

}
