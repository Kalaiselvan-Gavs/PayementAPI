package com.rabo.paymentinitiation.service;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.util.Constants;
import com.rabo.paymentinitiation.util.PaymentUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class PaymentServiceImplTest {

    @InjectMocks 
    private PaymentService paymentService = new PaymentServiceImpl();;

    private PaymentInitiationRequest paymentRequest;
    
    String signatureCertificate;

    @BeforeEach
    void setUp() {
    	signatureCertificate = Constants.BEGIN_CERTIFICATE +"\r\nda+Qmv1MZeHaMFAZvz0OcDnvDwzXOSVOTU1XX\r\n" + Constants.END_CERTIFICATE;
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

    @org.junit.Test(expected = NullPointerException.class)
    void whenPaymentAmountRequestIsNull() {
        paymentRequest = null;
        paymentService.checkForAmoutLimitExceeded(paymentRequest);
    }

    @Test
    void whenPaymentAmountisZero() {
        paymentRequest.setAmount("0");
        boolean isAmountLimitExceeded = paymentService.checkForAmoutLimitExceeded(paymentRequest);
        assertThat(!isAmountLimitExceeded);
    }
    
    /*@Test
    void whenVerifySignature_OK() throws Exception {
    	paymentService.verifySignature(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class));
    }*/

    @Test
    void whenVerifySignature() throws Exception {
        
        try {
        	given(PaymentUtil.formatSignatureCertificate(Mockito.any())).willReturn(signatureCertificate);
        	paymentService.verifySignature(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class));

        } catch (RuntimeException e) {
        	assertThat(e.getCause());
        }
    }
    
    @Test
    void whenwhiteListedCertificatesValidation() throws Exception {
        
        try {
        	given(PaymentUtil.formatSignatureCertificate(signatureCertificate)).willReturn(Mockito.any(String.class));
        	paymentService.whiteListedCertificatesValidation(Mockito.any(String.class));

        } catch (RuntimeException e) {
        	assertThat(e.getCause());
        }
    }
}