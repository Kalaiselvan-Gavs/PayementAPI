package com.rabo.paymentinitiation.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.service.PaymentServiceImpl;
import com.rabo.paymentinitiation.util.Constants;

//import com.in28minutes.springboot.model.Course;
//import com.in28minutes.springboot.service.StudentService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PaymentInitiationController.class)
//@WithMockUser
public class PaymentInitiationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PaymentService paymentService;

	private ObjectMapper objectMappaer;
    
    private PaymentInitiationRequest paymentRequest;
   
    
    @Before
    public void init() {
    	this.mockMvc = MockMvcBuilders.standaloneSetup(new PaymentInitiationController()).build();
    	objectMappaer = new ObjectMapper();
    	paymentRequest = new PaymentInitiationRequest();
    	paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");
        
    }
    
	@Test
	public void createPaymentTest() throws Exception {

		Mockito.when(paymentService.checkForAmoutLimitExceeded(Mockito.anyObject())).thenReturn(false);
		
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
	}

	@Test
	public void createPaymentFailTest() throws Exception {

		Mockito.when(paymentService.checkForAmoutLimitExceeded(Mockito.anyObject())).thenReturn(false);
		
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
	}

}

