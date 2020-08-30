package com.rabo.paymentinitiation.model;

public enum ErrorReasonCode {
    UNKNOWN_CERTIFICATE("UNKNOWN CERTIFICATE"),
    INVALID_SIGNATURE("INVALID SIGNATURE"),
    INVALID_REQUEST("INVALID REQUEST"),
    LIMIT_EXCEEDED("LIMIT EXCEEDED"),
    GENERAL_ERROR("GENERAL ERROR");
    
    private String value;

	private ErrorReasonCode(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
