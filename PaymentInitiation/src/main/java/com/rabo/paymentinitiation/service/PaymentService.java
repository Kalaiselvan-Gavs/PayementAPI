package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface PaymentService {

	public boolean checkForAmoutLimitExceeded(PaymentInitiationRequest paymentInitiationRequest);
	public boolean verifySignature(String xRequestId, String requestBody) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
	
}
