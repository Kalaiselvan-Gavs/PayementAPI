package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;

public interface PaymentService {

	public boolean checkForAmoutLimitExceeded(PaymentInitiationRequest paymentInitiationRequest);
	public void verifySignature(String xRequestId, String requestBody, String signatureCertificate, String signature) throws Exception;
	public void whiteListedCertificatesValidation(String signatureCertificate) throws Exception;
	
}
