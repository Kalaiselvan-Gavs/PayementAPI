package com.rabo.paymentinitiation.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class PaymentInitiationRequestTest {
	private PaymentInitiationRequest paymentInitiationRequest;

    @Before
    public void init(){
    	paymentInitiationRequest = new PaymentInitiationRequest();
    	paymentInitiationRequest.setDebtorIBAN("1");
    	paymentInitiationRequest.setCreditorIBAN("1");
    	paymentInitiationRequest.setEndToEndId("1");
    	paymentInitiationRequest.setAmount("1");
    }

    @Test
    public void whenPaymentwitoutDebtor() {
    	paymentInitiationRequest.setDebtorIBAN("");
        assertEquals("", paymentInitiationRequest.getDebtorIBAN());
    }

    @Test
    public void whenAmountIsEmpty() {
    	paymentInitiationRequest.setAmount("");
        assertEquals("", paymentInitiationRequest.getAmount());
    }
    
    @Test
    public void whenPaymentWithoutCreditor() {
    	paymentInitiationRequest.setCreditorIBAN("");
        assertEquals("", paymentInitiationRequest.getCreditorIBAN());
    }
    
    @Test
    public void whenPaymentWithoutEndToEndId() {
    	paymentInitiationRequest.setEndToEndId("");
        assertEquals("", paymentInitiationRequest.getEndToEndId());
    }
    
    @Test
    public void toStringCheck() {
    	assertNotNull(paymentInitiationRequest.toString());
    }
}
