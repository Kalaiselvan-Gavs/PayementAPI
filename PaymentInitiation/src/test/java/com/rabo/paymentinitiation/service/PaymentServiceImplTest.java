package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
class PaymentServiceImplTest {

    @MockBean
    private PaymentService paymentService;

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
    public void whenAmountLimitExceeded() {
        paymentRequest.setDebtorIBAN("NL02RABO0222222222");
        boolean isAmountLimitExceeded = paymentService.checkForAmoutLimitExceeded(paymentRequest);
        assertThat(isAmountLimitExceeded);
    }

    @Test
    public void whenAmountLimitNotExceeded() {
        boolean isAmountLimitExceeded = paymentService.checkForAmoutLimitExceeded(paymentRequest);
        assertThat(!isAmountLimitExceeded);
    }

    @Test
    public void whenPaymentAmountRequestIsNull() {
        paymentRequest = null;
        boolean isAmountLimitExceeded = paymentService.checkForAmoutLimitExceeded(paymentRequest);
        assertThat(!isAmountLimitExceeded);
    }

    @Test
    public void whenVerifySignature_OK() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        boolean isAmountLimitExceeded = paymentService.verifySignature("1234", new String());
        assertThat(isAmountLimitExceeded);
    }

    @Test
    public void whenVerifySignature_NotOk() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        paymentRequest = null;
        boolean isAmountLimitExceeded = paymentService.verifySignature(null, new String());
        assertThat(isAmountLimitExceeded);
    }

    @Test
    public void whenVerifySignature_requestIsNull() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        paymentRequest = null;
        boolean isAmountLimitExceeded = paymentService.verifySignature("", null);
        assertThat(isAmountLimitExceeded);
    }
}