package com.rabo.paymentinitiation.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.filter.CustomFilter;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.util.Constants;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PaymentInitiationController.class, excludeAutoConfiguration = CustomFilter.class)
public class PaymentInitiationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentService service;
    
    private ObjectMapper objectMappaer;
    
    PaymentInitiationRequest paymentRequest;

    @Before
    public void setUp() throws Exception {
    	objectMappaer = new ObjectMapper();
    	paymentRequest = new PaymentInitiationRequest();
    	paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");
    }

    @Test
    public void whenPaymentInitiationSuccess() throws Exception {
        given(service.checkForAmoutLimitExceeded(Mockito.any())).willReturn(false);

        mvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMappaer.writeValueAsString(paymentRequest))
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
        		.header(Constants.SIGNATURE, "3"))
        		.andExpect(status().isCreated());
        reset(service);
    }
    
    @Test
    public void whenCheckForAmoutLimitExceeded() throws Exception {
    	paymentRequest.setDebtorIBAN("NL02RABO0022222222");
        given(service.checkForAmoutLimitExceeded(Mockito.any())).willReturn(true);

        mvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMappaer.writeValueAsString(paymentRequest))
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
        		.header(Constants.SIGNATURE, "3"))
        		.andExpect(status().isUnprocessableEntity());
        reset(service);
    }
    
    @Test
    public void whenCheckForEmptyDebtorIBAN() throws Exception {
    	paymentRequest.setDebtorIBAN("");
        given(service.checkForAmoutLimitExceeded(Mockito.any())).willReturn(true);

        mvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMappaer.writeValueAsString(paymentRequest))
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
        		.header(Constants.SIGNATURE, "3"))
        		.andExpect(status().is4xxClientError());
        reset(service);
    }
    
    @Test
    public void whenCheckForEmptyCreditorIBAN() throws Exception {
    	paymentRequest.setCreditorIBAN("");
        given(service.checkForAmoutLimitExceeded(Mockito.any())).willReturn(false);

        mvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMappaer.writeValueAsString(paymentRequest))
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
        		.header(Constants.SIGNATURE, "3"))
        		.andExpect(status().is4xxClientError());
        reset(service);
    }
    
    @Test
    public void whenCheckForAmountEmpty() throws Exception {
    	paymentRequest.setAmount("");
        given(service.checkForAmoutLimitExceeded(Mockito.any())).willReturn(false);

        mvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMappaer.writeValueAsString(paymentRequest))
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
        		.header(Constants.SIGNATURE, "3"))
        		.andExpect(status().is4xxClientError());
        reset(service);
    }
    
    @Test
    public void whenCheckForInvalidAmount() throws Exception {
    	paymentRequest.setAmount("a.00");
        given(service.checkForAmoutLimitExceeded(Mockito.any())).willReturn(false);

        mvc.perform(post("/payment/v1.0.0/initiate-payment")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMappaer.writeValueAsString(paymentRequest))
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
        		.header(Constants.SIGNATURE, "3"))
        		.andExpect(status().is4xxClientError());
        reset(service);
    }
    
}
