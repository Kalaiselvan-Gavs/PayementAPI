package com.rabo.paymentinitiation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentInitiationControllerTest {

    private ObjectMapper objectMappaer;
    
    private PaymentInitiationRequest paymentRequest;

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private PaymentService paymentService;

    @Before
    public void init() {
    	objectMappaer = new ObjectMapper();
    	paymentRequest = new PaymentInitiationRequest();
    	paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");
    }

    @Test
    public void create_Payment_OK() throws Exception {

    	mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void create_Payment_AmountExceeded() throws Exception {

    	paymentRequest.setDebtorIBAN("NL02RABO0222222222");
    	
    	mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void create_Payment_SignaureVerified() throws Exception {

    	boolean isVerified = true;
    	
    	when(paymentService.verifySignature(any(String.class), any(String.class))).thenReturn(isVerified);

    	mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void create_Payment_SignaureNotVerified() throws Exception {

    	boolean isVerified = false;
    	
    	when(paymentService.verifySignature(any(String.class), any(String.class))).thenReturn(isVerified);

    	mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void whenDebtorIBANNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setDebtorIBAN(null);
        
        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
        		.andExpect(status().is4xxClientError());
    }
    
    @Test
    public void whenCreditorIBANNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setCreditorIBAN(null);
        
        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
        		.andExpect(status().is4xxClientError());
    }
    
    @Test
    public void whenAmountNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setAmount(null);
        
        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
        		.andExpect(status().is4xxClientError());
    }
    
    @Test
    public void whenEndtoEndIdNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setEndToEndId(null);
        
        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
        		.andExpect(status().is4xxClientError());
    }
    
    @Test
    public void whenRequestId_Header_Missing_thenReturns400Error() throws Exception {
    	
        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.Signature_Certificate, "2")
				.header(Constants.Signature, "3"))
        		.andExpect(status().is4xxClientError());
    }
    
    @Test
    public void whenSignature_Header_Missing_thenReturns400Error() throws Exception {
    	
        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature_Certificate, "2"))
        		.andExpect(status().is4xxClientError());
    }
    
    @Test
    public void whenSignatureCer_Header_Missing_thenReturns400Error() throws Exception {
    	
        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .content(objectMappaer.writeValueAsString(paymentRequest))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
				.header(Constants.Signature, "3"))
        		.andExpect(status().is4xxClientError());
    }
    
}