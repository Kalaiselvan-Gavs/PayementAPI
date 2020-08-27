package com.rabo.paymentinitiation.controller;

import java.util.UUID;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/payment/" + Constants.API_VERSION)
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
	@ApiResponses(value = { @ApiResponse(code = 201, message = Constants.PAYMENT_ACCEPTED),
			@ApiResponse(code = 400, message = Constants.PAYMENT_REJECTED),
			@ApiResponse(code = 422, message = Constants.PAYMENT_REJECTED),
			@ApiResponse(code = 500, message = Constants.INTERNAL_SERVER), })
	public ResponseEntity<Object> processPayment(@RequestHeader(name = Constants.X_REQUEST_ID, required = true) String requestId,
			@RequestHeader(name = Constants.Signature_Certificate, required = true) String signatureCertificate,
			@RequestHeader(name = Constants.Signature, required = true) String signature,
			@Valid @RequestBody PaymentInitiationRequest paymentInitiationRequest) {
		
		log.info("Enter PaymentInitiationController :: processPayment");
		
		//Amount limit exceeded
		if(paymentService.checkForAmoutLimitExceeded(paymentInitiationRequest)) {
			throw new RuntimeException(ErrorReasonCode.LIMIT_EXCEEDED.name());
		}
		
		
		PaymentAcceptedResponse paymentAcceptedResponse = new PaymentAcceptedResponse();
		paymentAcceptedResponse.setPaymentId(UUID.randomUUID().toString());
		paymentAcceptedResponse.setStatus(TransactionStatus.ACCEPTED);
		
		HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.X_REQUEST_ID, requestId);
        headers.add(Constants.Signature_Certificate, signatureCertificate);
        headers.add(Constants.Signature, signature);
        
		return new ResponseEntity<>(paymentAcceptedResponse, headers, HttpStatus.CREATED);
	}

}
