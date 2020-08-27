package com.rabo.paymentinitiation.filter;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;
import com.rabo.paymentinitiation.model.ErrorReasonCode;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.util.Constants;

@Configuration
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
		
		byte[] byteArray = StreamUtils.copyToByteArray(wrappedRequest.getInputStream());
		String requestBody = new String(byteArray);
		
		try {
			if(!paymentService.verifySignature(xRequestId, requestBody)) {
				throw new RuntimeException(ErrorReasonCode.INVALID_SIGNATURE.name());
			}
		} catch (Exception e) {
			log.error("Exception in Signature verification : ", e);
		}
		
		log.info("Logging Request Body : {} ", requestBody);
		log.info("Logging Request  {} : {}", wrappedRequest.getMethod(), wrappedRequest.getRequestURI());
		
		// call next filter in the filter chain
		filterChain.doFilter(wrappedRequest, response);
		
		log.info("Logging Response :{}", response.getContentType());
		
	}
	
	@Override
	public void destroy() {
		log.info("########## Destroying Custom filter ##########");
	}
}
//Ref : https://stackoverflow.com/questions/4449096/how-to-read-request-getinputstream-multiple-times/46824271