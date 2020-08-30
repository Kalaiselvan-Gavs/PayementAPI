package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.exception.GeneralException;
import com.rabo.paymentinitiation.exception.InvalidException;
import com.rabo.paymentinitiation.model.ErrorReasonCode;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
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
	public void whiteListedCertificatesValidation(String signatureCertificate) {
		//Get the Certificate
		X509Certificate certificate = getX509Certificate(signatureCertificate);
		
		Principal subjectDN = certificate.getSubjectDN(); 
		if(null != subjectDN && !subjectDN.getName().contains("Sandbox-TPP")) {
			throw new InvalidException(ErrorReasonCode.UNKNOWN_CERTIFICATE.name());
		}
		log.info("####### Known Certificate ######");
	}
	
	/**
	 * Verify the signature
	 */
	public void verifySignature(String xRequestId, String requestBody, String signatureCertificate, String signature) {
		
		//Get the Certificate
		X509Certificate certificate = getX509Certificate(signatureCertificate);
		RSAPublicKey  publicKey = (RSAPublicKey) certificate.getPublicKey();
		
        try {
        	
        	//Read signature Attribute
            byte[] encryptedMessageHash = signature.getBytes(StandardCharsets.UTF_8);//Files.readAllBytes(Paths.get("target/digital_signature_2"));

            Signature rsaSignature = Signature.getInstance("SHA256withRSA");//SIGNING_ALGORITHM
            rsaSignature.initVerify(publicKey);

            //Read RequestBody
            byte[] messageBytes = (xRequestId + requestBody).getBytes(StandardCharsets.UTF_8);//Files.readAllBytes(Paths.get("src/test/resources/digitalsignature/message.txt"));
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] newMessageHash = md.digest(messageBytes);
            
            rsaSignature.update(newMessageHash);
            
        	//Signature validation
            if(!rsaSignature.verify(encryptedMessageHash)) {
            	throw new InvalidException(ErrorReasonCode.INVALID_SIGNATURE.name());
            }
            
        } catch(Exception ex) {
        	throw new GeneralException(ErrorReasonCode.GENERAL_ERROR.name());
        }
        
	}
	
	/**
	 * Get certificate
	 * @param signatureCertificate
	 * @return
	 */
	private X509Certificate getX509Certificate(String signatureCertificate) {
		
		StringBuilder formatCertificateBuffer = new StringBuilder();
		formatCertificateBuffer.append("-----BEGIN CERTIFICATE-----\r\n");
		formatCertificateBuffer.append(signatureCertificate);
		formatCertificateBuffer.append("\r\n-----END CERTIFICATE-----");
		
		InputStream result = new ByteArrayInputStream(formatCertificateBuffer.toString().getBytes(StandardCharsets.UTF_8));
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
