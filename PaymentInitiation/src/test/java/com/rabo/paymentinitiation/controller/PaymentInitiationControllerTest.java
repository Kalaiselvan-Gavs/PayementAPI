package com.rabo.paymentinitiation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PaymentInitiationController.class)
class PaymentInitiationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenValidInput_thenReturns200() throws Exception {
        PaymentInitiationRequest paymentRequest = new PaymentInitiationRequest();
        paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void whenDebtorIBANNullValue_thenReturns400Error() throws Exception {
        PaymentInitiationRequest paymentRequest = new PaymentInitiationRequest();
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreditorIBANNullValue_thenReturns400Error() throws Exception {
        PaymentInitiationRequest paymentRequest = new PaymentInitiationRequest();
        paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAmountNullValue_thenReturns400Error() throws Exception {
        PaymentInitiationRequest paymentRequest = new PaymentInitiationRequest();
        paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setEndToEndId("1");

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenEndtoEndIdNullValue_thenReturns400Error() throws Exception {
        PaymentInitiationRequest paymentRequest = new PaymentInitiationRequest();
        paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");

        mockMvc.perform(post("/payment/v1.0.0/initiate-payment")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest());
    }
}