package com.rabo.paymentinitiation.model;

public class PaymentRejectedResponse {
	
    private TransactionStatus status;
    
    private String reason;
    
    private ErrorReasonCode reasonCode;
    
	public TransactionStatus getStatus() {
		return status;
	}
	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public ErrorReasonCode getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(ErrorReasonCode reasonCode) {
		this.reasonCode = reasonCode;
	}
	
	@Override
	public String toString() {
		return "PaymentRejectedResponse [status=" + status + ", reason=" + reason + ", reasonCode=" + reasonCode
				+ ", getStatus()=" + getStatus() + ", getReason()=" + getReason() + ", getReasonCode()="
				+ getReasonCode() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
}
