package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.PaymentUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import java.security.*;
import java.util.Arrays;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Value( "${key.store.password}" )
	private String keyStorePassword;
	
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
	public boolean verifySignature(String xRequestId, String requestBody) throws Exception {
		
		PaymentUtil.setKeyStorePassword(keyStorePassword);
		PublicKey publicKey = PaymentUtil.getPublicKey();
		PrivateKey privateKey = PaymentUtil.getPrivateKey();

		byte[] messageBytes = (xRequestId + requestBody).getBytes();

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] newMessageHash = md.digest(messageBytes);

		//Encryption
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] digitalSignatureEncryption = cipher.doFinal(newMessageHash);

		//Decryption
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] decryptedMessageHash = cipher.doFinal(digitalSignatureEncryption);

		return Arrays.equals(decryptedMessageHash, newMessageHash);
	}
	
}
