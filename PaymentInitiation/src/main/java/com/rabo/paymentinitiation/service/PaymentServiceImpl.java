package com.rabo.paymentinitiation.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

@Service
public class PaymentServiceImpl implements PaymentService {

	/**
	 * Sum all the digits & omitted letters. 
	 */
	public int sumOfDigits(String input) {
		
		Pattern pattern = Pattern.compile("[\\d]");
	    Matcher matcher = pattern.matcher(input);
	    int sum = 0;
	    while(matcher.find()) {
	        sum += Integer.parseInt(matcher.group());
	    }
	    return sum;
	}

	/**
	 * Verify the signature
	 */
	public boolean verifySignature(String xRequestId, String requestBody) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); // KeySize
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		 
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		 
		byte[] data = (xRequestId + requestBody).getBytes();
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(data);
		byte[] signedData = signature.sign();
		 
		signature.initVerify(publicKey);
		signature.update(data);
		
		return signature.verify(signedData);
	}
	
}
