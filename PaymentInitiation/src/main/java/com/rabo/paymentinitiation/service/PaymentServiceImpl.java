package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.PaymentUtil;
import com.rabo.paymentinitiation.util.Utils;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Arrays;

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
	public boolean verifySignature(String xRequestId, String requestBody) throws Exception {
		/*KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
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
		
		return signature.verify(signedData);*/
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

		boolean isCorrect = Arrays.equals(decryptedMessageHash, newMessageHash);
		System.out.println("Signature " + (isCorrect ? "correct" : "incorrect"));
		return isCorrect;
	}
	
}
