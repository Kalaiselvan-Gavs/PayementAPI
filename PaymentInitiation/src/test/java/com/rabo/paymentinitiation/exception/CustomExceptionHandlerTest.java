package com.rabo.paymentinitiation.exception;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.WebRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.controller.PaymentInitiationController;
import com.rabo.paymentinitiation.model.ErrorReasonCode;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomExceptionHandlerTest {
	private MockMvc mockMvc;

    @Mock
    PaymentInitiationController paymentInitiationController;
    
    @MockBean
    CustomExceptionHandler customExceptionHandler;
    		
    private PaymentInitiationRequest paymentRequest;
    
    private ObjectMapper objectMappaer;
    
    
    @MockBean
    private WebRequest webRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentInitiationController).setControllerAdvice(new CustomExceptionHandler())
            .build();
        objectMappaer = new ObjectMapper();
        paymentRequest = new PaymentInitiationRequest();
    	paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");
    }

    @Test
    public void whenHandleHeaderException() throws Exception {

        Mockito.when(paymentInitiationController.processPayment("", "", "", paymentRequest)).thenThrow(new RuntimeException("Unexpected Exception"));

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")).andExpect(status().is(400));

    }
    
    @Test
    public void whenHandleNoException() throws Exception {

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")).andExpect(status().is(400));
        this.mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
        		.header("Authorization", "Bearer foo")
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3"))
        		.andExpect(status().is4xxClientError());
 
    }
    
    
    @Test
    public void whenHandlewithBeanValidation() throws Exception {
    	paymentRequest.setDebtorIBAN("O7134384551");
    	
        this.mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3"))
        		.andExpect(status().is4xxClientError());
    }
    
    
    
    @Test
    public void whenHandleInvalidException() throws Exception {
    	customExceptionHandler.handleInvalidException(new InvalidException(ErrorReasonCode.INVALID_SIGNATURE.name()), webRequest);
    	this.mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3"))
    			.andExpect(status().is4xxClientError());
    }
    
    @Test
    public void whenHandleGeneralException() throws Exception {
    	customExceptionHandler.handleGeneralException(new GeneralException(ErrorReasonCode.GENERAL_ERROR.name()), webRequest);
    	this.mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
				.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3"))
    			.andExpect(status().is4xxClientError());
    }
}
