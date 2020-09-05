package com.rabo.paymentinitiation.service;

import com.google.common.primitives.Bytes;
import com.rabo.paymentinitiation.exception.GeneralException;
import com.rabo.paymentinitiation.exception.InvalidException;
import com.rabo.paymentinitiation.model.ErrorReasonCode;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.Constants;
import com.rabo.paymentinitiation.util.PaymentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
		
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
	 * White listed certificates validation
	 * @param signatureCertificate
	 */
	public void whiteListedCertificatesValidation(String signatureCertificate)  throws Exception {
		//Get the Certificate
		X509Certificate certificate = getX509Certificate(signatureCertificate);
		
		Principal subjectDN = certificate.getSubjectDN(); 
		if(null != subjectDN && !subjectDN.getName().contains("Sandbox-TPP")) {
			log.info("####### UnKnown Certificate ######");
			throw new InvalidException(ErrorReasonCode.UNKNOWN_CERTIFICATE.name());
		}
	}
	
	/**
	 * Verify the signature
	 */
	public void verifySignature(String xRequestId, String requestBody, String signatureCertificate, String signature)  throws Exception {

		boolean isValidSignature = false;

		try {
			// Get the Certificate
			X509Certificate certificate = getX509Certificate(signatureCertificate);
			RSAPublicKey publicKey = (RSAPublicKey) certificate.getPublicKey();
			
			// Read signature header
			byte[] encryptedSignatureBytes = signature.getBytes(StandardCharsets.UTF_8);
			byte[] decryptedSignatureHash = Base64.getDecoder().decode(encryptedSignatureBytes);

			// Read RequestBody
			byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);

			byte[] requestIdBytes = xRequestId.getBytes(StandardCharsets.UTF_8);

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] requestBodyHash = md.digest(requestBodyBytes);

			// Compute Hash
			byte[] computeSignatureHash = Bytes.concat(requestIdBytes, requestBodyHash);

			// Create Signature
			Signature rsaSignature = Signature.getInstance("SHA256withRSA");
			rsaSignature.initVerify(publicKey);

			rsaSignature.update(computeSignatureHash);

			// Signature validation
			isValidSignature = rsaSignature.verify(decryptedSignatureHash);

		} catch (Exception ex) {
			throw new GeneralException(ErrorReasonCode.GENERAL_ERROR.name());
		}
		
		if(!isValidSignature) {
			throw new InvalidException(ErrorReasonCode.INVALID_SIGNATURE.name());
		}
	}
	
	/**
	 * Get certificate
	 * @param signatureCertificate
	 * @return
	 */
	private X509Certificate getX509Certificate(String signatureCertificate)  throws Exception {
		
		String formatCertificate = PaymentUtil.formatSignatureCertificate(signatureCertificate);
		InputStream result = new ByteArrayInputStream(formatCertificate.getBytes(StandardCharsets.UTF_8));
		CertificateFactory certificateFactory;
		X509Certificate certificate;
		
		try {
			certificateFactory = CertificateFactory.getInstance("X.509");
			certificate = (X509Certificate) certificateFactory.generateCertificate(result);
		} catch (CertificateException e) {
			throw new GeneralException(ErrorReasonCode.GENERAL_ERROR.name());
		}
		
		return certificate;
	}
	
}
