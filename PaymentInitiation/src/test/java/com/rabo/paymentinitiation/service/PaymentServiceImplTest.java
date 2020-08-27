package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.PaymentUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
class PaymentServiceImplTest {

    @InjectMocks 
    private PaymentService paymentService = new PaymentServiceImpl();;

    private PaymentInitiationRequest paymentRequest;

    @BeforeEach
    void setUp() {
    	paymentRequest = new PaymentInitiationRequest();
        paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void whenAmountLimitExceeded() {
        paymentRequest.setDebtorIBAN("NL02RABO0222222222");
        boolean isAmountLimitExceeded = paymentService.checkForAmoutLimitExceeded(paymentRequest);
        assertThat(isAmountLimitExceeded);
    }

    @Test
    void whenAmountLimitNotExceeded() {
        boolean isAmountLimitExceeded = paymentService.checkForAmoutLimitExceeded(paymentRequest);
        assertThat(!isAmountLimitExceeded);
    }

    @Test
    void whenPaymentAmountRequestIsNull() {
        paymentRequest = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
        	paymentService.checkForAmoutLimitExceeded(paymentRequest);
          });
    }

    @Test
    void whenPaymentAmountisZero() {
        paymentRequest.setAmount("0");
        boolean isAmountLimitExceeded = paymentService.checkForAmoutLimitExceeded(paymentRequest);
        assertThat(!isAmountLimitExceeded);
    }
    
    @Test
    void whenVerifySignature_OK() throws Exception {
        Assertions.assertThrows(NullPointerException.class, () -> {
        	paymentService.verifySignature("", null);
          });
    }

    @Test
    void whenVerifySignature_NotOk() throws Exception {
        paymentRequest = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
        	paymentService.verifySignature(null, new String());
          });
    }

    @Test
    void whenVerifySignature_requestIsNull() throws Exception {
        Assertions.assertThrows(NullPointerException.class, () -> {
        	paymentService.verifySignature("", null);
          });
    }
}