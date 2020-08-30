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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.util.Constants;

@Configuration
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
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
		
		byte[] byteArray = StreamUtils.copyToByteArray(wrappedRequest.getInputStream());
		String requestBody = new String(byteArray);
		
		log.info("Logging Request Body : {} ", requestBody);
		
		//White listed certificates validation
		paymentService.whiteListedCertificatesValidation(signatureCertificate);
		
		//Signature validation
		paymentService.verifySignature(xRequestId, requestBody, signatureCertificate, signature);
		
		// call next filter in the filter chain
		filterChain.doFilter(wrappedRequest, response);

	}
	
	@Override
	public void destroy() {
		log.info("########## Destroying Custom filter ##########");
	}
}
//Ref : https://stackoverflow.com/questions/4449096/how-to-read-request-getinputstream-multiple-times/46824271