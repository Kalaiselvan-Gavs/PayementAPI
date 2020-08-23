package com.rabo.paymentinitiation.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.controller.PaymentInitiationController;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SecurityConfigTest {
    
	private ObjectMapper objectMappaer;
    
    private PaymentInitiationRequest paymentRequest;

    @Autowired
    private PaymentInitiationController paymentInitiationController;
    
	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() throws Exception {
	    assertThat(paymentInitiationController).isNotNull();
	}
	
	
	@Before
	public void setup() {
		objectMappaer = new ObjectMapper();
    	paymentRequest = new PaymentInitiationRequest();
    	paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");
	}

	@Test
	void untrustedClientShouldBeForbidden() throws Exception {

		this.mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
				.with(x509(getCertificateFromFile("classpath:ssl/untrusted-cert.pem")))
				.content(objectMappaer.writeValueAsString(paymentRequest))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
				.andExpect(status().is(HttpStatus.FORBIDDEN.value()));
	}

	private RequestPostProcessor x509(Object certificateFromFile) {
		return null;
	}


	private Object getCertificateFromFile(String string) {
		return null;
	}
}
