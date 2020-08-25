package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.PaymentUtil;
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
	 * Amount limit exceeded check
	 * @param paymentInitiationRequest
	 * @return
	 */
	@Override
	public boolean checkForAmoutLimitExceeded(PaymentInitiationRequest paymentInitiationRequest) {
		if(Double.parseDouble(paymentInitiationRequest.getAmount()) > 0
				&& (PaymentUtil.sumOfDigits(paymentInitiationRequest.getDebtorIBAN()) % paymentInitiationRequest.getDebtorIBAN().length() == 0)) {
			return true;
		}
		return false;
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
