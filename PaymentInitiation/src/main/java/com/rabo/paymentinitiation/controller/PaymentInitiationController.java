package com.rabo.paymentinitiation.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.UUID;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rabo.paymentinitiation.exception.ErrorResponse;
import com.rabo.paymentinitiation.model.ErrorReasonCode;
import com.rabo.paymentinitiation.model.PaymentAcceptedResponse;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.model.TransactionStatus;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.util.Constants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/payment/"+Constants.API_VERSION)
public class PaymentInitiationController {

	private final Logger log = LoggerFactory.getLogger(PaymentInitiationController.class);

	@Autowired
	private PaymentService paymentService;
	 
	/**
	 * To Initiate a payment for third party payment providers (TPPs)  
	 * @param paymentInitiationRequest
	 * @return
	 */
	
	@PostMapping("/initiate-payment")
	@ApiOperation(value = "Payment initiation API for third party payment providers (TPPs)", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {@ApiResponse(code = 201, message = Constants.PAYMENT_ACCEPTED),
			@ApiResponse(code = 400, message = Constants.PAYMENT_REJECTED),
			@ApiResponse(code = 422, message = Constants.PAYMENT_REJECTED),
			@ApiResponse(code = 500, message = Constants.INTERNAL_SERVER), })
	public ResponseEntity<Object> processPayment(@RequestHeader(name = "X-Request-Id", required = true) String reuestId,
			@RequestHeader(name = "Signature-Certificate", required = true) String signatureCertificate,
			@RequestHeader(name = "Signature", required = true) String signature,
			@Valid @RequestBody PaymentInitiationRequest paymentInitiationRequest) {
		
		log.info("Enter PaymentInitiationController :: processPayment");
		
		//Amount limit exceeded
		if(paymentService.sumOfDigits(paymentInitiationRequest.getDebtorIBAN()) % paymentInitiationRequest.getDebtorIBAN().length() == 0) {
			ErrorResponse error = new ErrorResponse(ErrorReasonCode.LIMIT_EXCEEDED.name());
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//Signature validation
		try {
			if(!paymentService.verifySignature(reuestId, paymentInitiationRequest.toString())) {
				ErrorResponse error = new ErrorResponse(ErrorReasonCode.INVALID_SIGNATURE.name());
				return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (InvalidKeyException e) {
			log.error("Signature verification : InvalidKeyException ", e);
		} catch (NoSuchAlgorithmException e) {
			log.error("Signature verification : NoSuchAlgorithmException ", e);
		} catch (SignatureException e) {
			log.error("Signature verification : SignatureException ", e);
		}
		
		PaymentAcceptedResponse paymentAcceptedResponse = new PaymentAcceptedResponse();
		paymentAcceptedResponse.setPaymentId(UUID.randomUUID().toString());
		paymentAcceptedResponse.setStatus(TransactionStatus.ACCEPTED);
		
		return new ResponseEntity<>(paymentAcceptedResponse, HttpStatus.CREATED);
	}

}
