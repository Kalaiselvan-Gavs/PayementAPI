package com.rabo.paymentinitiation.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;
import java.security.cert.X509Certificate;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.controller.PaymentInitiationController;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.util.Constants;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {
    
	private ObjectMapper objectMappaer;
    
    private PaymentInitiationRequest paymentRequest;

    @Autowired
	private MockMvc mockMvc;
	
	@MockBean
    private PaymentService paymentService;
	
	@Before
    public void init() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
    	this.mockMvc = MockMvcBuilders.standaloneSetup(new PaymentInitiationController()).build();
    	objectMappaer = new ObjectMapper();
    	paymentRequest = new PaymentInitiationRequest();
    	paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");
        //when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(false);
        //when(paymentService.verifySignature(any(String.class),any(String.class))).thenReturn(true);
        
    }

	@Test
	public void untrustedClientShouldBeForbidden() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}
	
	@Test
	public void trustedClientShouldBeCreated() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.with(x509(getCertificateFromFile("classpath:ssl/untrusted-cert.pem")))
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
	}

	private RequestPostProcessor x509(Object certificateFromFile) {
		return new RequestPostProcessor() {

	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            Principal principal = mock(Principal.class);
	            request.setUserPrincipal(principal);
	            return request;
	        }
	    };
	}


	private Object getCertificateFromFile(String string) {
		return paymentRequest;
	}
}
