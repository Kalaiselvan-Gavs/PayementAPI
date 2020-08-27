package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;

public interface PaymentService {

	public boolean checkForAmoutLimitExceeded(PaymentInitiationRequest paymentInitiationRequest);
	public boolean verifySignature(String xRequestId, String requestBody) throws Exception;
	
}
