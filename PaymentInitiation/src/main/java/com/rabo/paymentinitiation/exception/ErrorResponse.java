package com.rabo.paymentinitiation.exception;

import java.util.List;

public class ErrorResponse
{
	private String timeStamp;
    
    private int status;
    
    private String error;
    
    private String message = "";
    
    private List<String> details;

    public ErrorResponse(String message) {
        super();
        this.message = message;
    }
	
	public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }
	
	public ErrorResponse(String timeStamp, int status, String error, String message, List<String> details) {
        super();
        this.status = status;
        this.timeStamp = timeStamp;
        this.error = error;
        //this.message = message;
        this.details = details;
    }
	
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	} 
}
