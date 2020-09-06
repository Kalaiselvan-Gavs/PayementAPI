package com.rabo.paymentinitiation.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.model.ErrorReasonCode;
import com.rabo.paymentinitiation.model.PaymentRejectedResponse;
import com.rabo.paymentinitiation.model.TransactionStatus;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.util.Constants;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomFilter implements Filter {
	
	private static final Logger log = LoggerFactory.getLogger(CustomFilter.class);
	
	@Autowired
	private PaymentService paymentService;
	
	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
		log.info("########## Initiating Custom filter ##########");
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		MultiHttpServletRequestWrapper wrappedRequest = new MultiHttpServletRequestWrapper(request);
		
		//Reading request body, headers
		String xRequestId = wrappedRequest.getHeader(Constants.X_REQUEST_ID);
		String signatureCertificate = wrappedRequest.getHeader(Constants.SIGNATURE_CERTIFICATE);
		String signature = wrappedRequest.getHeader(Constants.SIGNATURE);
		
		try {
			byte[] byteArray = StreamUtils.copyToByteArray(wrappedRequest.getInputStream());
			String requestBody = new String(byteArray);
			
			log.info("Logging Request Body : {} ", requestBody);
			
			//White listed certificates validation
			paymentService.whiteListedCertificatesValidation(signatureCertificate);
			
			//Signature validation
			paymentService.verifySignature(xRequestId, requestBody, signatureCertificate, signature);
			
			// call next filter in the filter chain
			filterChain.doFilter(wrappedRequest, response);
		} catch (Exception exception) {
			
			final HttpServletResponseWrapper wrapperResponse = new HttpServletResponseWrapper(response);
			
			wrapperResponse.setStatus((ErrorReasonCode.valueOf(exception.getLocalizedMessage()) == ErrorReasonCode.GENERAL_ERROR) 
					? HttpStatus.INTERNAL_SERVER_ERROR.value() : HttpStatus.BAD_REQUEST.value());
			wrapperResponse.setHeader(Constants.X_REQUEST_ID, xRequestId);
			wrapperResponse.setHeader(Constants.SIGNATURE_CERTIFICATE, signatureCertificate);
			wrapperResponse.setHeader(Constants.SIGNATURE, signature);
			wrapperResponse.getWriter().write(convertObjectToJson(exception));
			wrapperResponse.getWriter().flush();
		}
	}
	
	public String convertObjectToJson(Exception exception) throws JsonProcessingException {
		
		PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();
        paymentRejectedResponse.setReason(ErrorReasonCode.valueOf(exception.getLocalizedMessage()).getValue());
        paymentRejectedResponse.setStatus(TransactionStatus.REJECTED);
        paymentRejectedResponse.setReasonCode(ErrorReasonCode.valueOf(exception.getLocalizedMessage()));
        
		ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(paymentRejectedResponse);
	}
	
	@Override
	public void destroy() {
		log.info("########## Destroying Custom filter ##########");
	}
}