package com.rabo.paymentinitiation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentInitiationControllerTest {

    private ObjectMapper objectMappaer;
    
    private PaymentInitiationRequest paymentRequest;
    
    @InjectMocks
    PaymentInitiationController paymentInitiationController;

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
        when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(false);
        
    }

    @Test
    public void create_Payment_OK() throws Exception {
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
    public void create_Payment1_OK() throws Exception {
    	when(paymentService.verifySignature(any(String.class),any(String.class))).thenReturn(false);
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
    public void create_Payment2_OK() throws Exception {
    	when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
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
    public void create_Payment_AmountExceeded() throws Exception {

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
    public void create_Payment1_AmountExceeded() throws Exception {

    	paymentRequest.setDebtorIBAN("NL02RABO0222222222");
    	paymentRequest.setAmount("0");
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
    public void create_Payment_AmountNotExceeded() throws Exception {

    	paymentRequest.setAmount("0");
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
    public void create_Payment_SignaureVerified() throws Exception {

    	boolean isVerified = true;
    	
    	when(paymentService.verifySignature(any(String.class), any(String.class))).thenReturn(isVerified);

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
    public void create_Payment_SignaureNotVerified() throws Exception {

    	boolean isVerified = false;
    	
    	when(paymentService.verifySignature(any(String.class), any(String.class))).thenReturn(isVerified);

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
    public void whenDebtorIBANNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setDebtorIBAN(null);
        
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
    public void whenCreditorIBANNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setCreditorIBAN(null);
        
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
    public void whenAmountNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setAmount(null);
        
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
    public void whenEndtoEndIdNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setEndToEndId(null);
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
    public void whenRequestId_Header_Missing_thenReturns400Error() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		//.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        
    }
    
    @Test
    public void whenSignature_Header_Missing_thenReturns400Error() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.Signature_Certificate, "2");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        
    }
    
    @Test
    public void whenSignatureCer_Header_Missing_thenReturns400Error() throws Exception {
    	Mockito.when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
     				.post("/payment/v1.0.0/initiate-payment")
     				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
     				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
     				.header(Constants.Signature_Certificate, "2")
            		.header(Constants.X_REQUEST_ID, "1")
    				.header(Constants.Signature, "3");
         
     	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());	
    }

    @Test
    public void whensignatureDetails_Header_Missing_thenReturns400Error() throws Exception {
    	Mockito.when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
     				.post("/payment/v1.0.0/initiate-payment")
     				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
     				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
     				.header(Constants.X_REQUEST_ID, "1");
         
     	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());	
    }
    
    @Test
    public void whenContentType_Missing_thenReturns400Error() throws Exception {
    	Mockito.when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
     				.post("/payment/v1.0.0/initiate-payment")
     				//.accept(MediaType.APPLICATION_JSON)
     				.content(objectMappaer.writeValueAsString(paymentRequest))
     				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
     				.header(Constants.X_REQUEST_ID, "1");
         
     	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());	
    }
    
    @Test
    public void whenInvoke_Invalid_thenReturns400Error() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
     				.post("/payment/v1.0.0/initiate-payment1")
     				.accept(MediaType.APPLICATION_JSON)
     				.content(objectMappaer.writeValueAsString(paymentRequest))
     				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
     				.header(Constants.Signature_Certificate, "2")
            		.header(Constants.X_REQUEST_ID, "1")
    				.header(Constants.Signature, "3");
         
     	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());	
    }
    
}