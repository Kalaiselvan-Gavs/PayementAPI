package com.rabo.paymentinitiation.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.Constants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/payment/v1.0.0")
public class PaymentInitiationController {

	private final Logger log = LoggerFactory.getLogger(PaymentInitiationController.class);

	/**
	 * To Initiate a payment for third party payment providers (TPPs)  
	 * @param paymentInitiationRequest
	 * @return
	 */
	@PostMapping("/initiate-payment")
	@ApiOperation(value = "Payment initiation API for third party payment providers (TPPs)", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS_MSG),
			@ApiResponse(code = 201, message = Constants.PAYMENT_ACCEPTED),
			@ApiResponse(code = 400, message = Constants.PAYMENT_REJECTED),
			@ApiResponse(code = 422, message = Constants.PAYMENT_REJECTED),
			@ApiResponse(code = 500, message = Constants.INTERNAL_SERVER), })
	public ResponseEntity<?> processPayment(@RequestHeader(name = "X-Request-Id", required = true) String reuestId,
			@RequestHeader(name = "Signature-Certificate", required = true) String signatureCertificate,
			@RequestHeader(name = "Signature", required = true) String signature,
			@Valid @RequestBody PaymentInitiationRequest paymentInitiationRequest) {
		
		log.info("Enter PaymentInitiationController :: processPayment");
		
		//Amount limit exceeded
		if(sumOfDigits(paymentInitiationRequest.getDebtorIBAN()) % paymentInitiationRequest.getDebtorIBAN().length() == 0) {
			return new ResponseEntity<>("Amount limit exceeded", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//TODO : 1.White listed certificates validation
		
		//TODO : 2.	Signature validation
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private int sumOfDigits(String input) {
		Pattern pattern = Pattern.compile("[\\d]");
	    Matcher matcher = pattern.matcher(input);
	    int sum = 0;
	    while(matcher.find()) {
	        sum += Integer.parseInt(matcher.group());
	    }
	    return sum;
	}
	
	// Certificate CommonName  (CN)  ===> https://www.programcreek.com/java-api-examples/?class=java.security.cert.X509Certificate&method=getSubjectX500Principal
	//https://stackoverflow.com/questions/6179450/is-there-a-way-to-recover-the-common-name-of-a-client-certificate-from-java-code
	//https://www.baeldung.com/x-509-authentication-in-spring-security
	//--> https://stackoverflow.com/questions/7933468/parsing-the-cn-out-of-a-certificate-dn
	
	//https://stackoverflow.com/questions/18669041/how-to-extract-cn-from-x509certificate-in-java-without-using-bouncy-castle
	//
	
}
