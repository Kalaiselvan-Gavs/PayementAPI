package com.rabo.paymentinitiation.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;
import javax.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.controller.PaymentInitiationController;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomExceptionHandlerTest {
	private MockMvc mockMvc;

    @Mock
    PaymentInitiationController paymentInitiationController;
    
    private PaymentInitiationRequest paymentRequest;
    
    private ObjectMapper objectMappaer;

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
    public void whenHandleConstraintViolationWithEmpty() throws Exception {
    	
        Mockito.when(paymentInitiationController.processPayment("", "", "", paymentRequest)).thenThrow(new ConstraintViolationException(Collections.emptySet()));

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")).andExpect(status().is(400));

    }
    
    
    @Test
    public void whenHandleRunTimeException() throws Exception {

        Mockito.when(paymentInitiationController.processPayment("", "", "", paymentRequest)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")).andExpect(status().is(400));

    }
    
    @Test
    public void whenRunTimeException() throws Exception {
    	paymentRequest.setDebtorIBAN("NL02RABO0222222222");
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
    public void when() throws Exception {
    	paymentRequest.setDebtorIBAN("NL02RABO0222222222");
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
    public void whenHandleUserNotFound() throws Exception {

        Mockito.when(paymentInitiationController.processPayment("", "", "", paymentRequest)).thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")).andExpect(status().is(400));

    }

}
