package com.rabo.paymentinitiation.model;

public class PaymentAcceptedResponse {
	
	private String paymentId;
    
    private TransactionStatus status;
    
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public TransactionStatus getStatus() {
		return status;
	}
	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "PaymentAcceptedResponse [paymentId=" + paymentId + ", status=" + status + ", getPaymentId()="
				+ getPaymentId() + ", getStatus()=" + getStatus() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

}
