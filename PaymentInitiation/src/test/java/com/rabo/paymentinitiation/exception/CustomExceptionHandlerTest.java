package com.rabo.paymentinitiation.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.rabo.paymentinitiation.controller.PaymentInitiationController;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomExceptionHandlerTest {
	private MockMvc mockMvc;

    @Mock
    PaymentInitiationController paymentInitiationController;
    
    private PaymentInitiationRequest paymentRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentInitiationController).setControllerAdvice(new CustomExceptionHandler())
            .build();
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

        Mockito.when(paymentInitiationController.processPayment("", "", "", paymentRequest)).thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")).andExpect(status().is(400));

    }


}
