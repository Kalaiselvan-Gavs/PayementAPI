package com.rabo.paymentinitiation.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface PaymentService {

	public int sumOfDigits(String input);
	
	public boolean verifySignature(String xRequestId, String requestBody) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
	
}
