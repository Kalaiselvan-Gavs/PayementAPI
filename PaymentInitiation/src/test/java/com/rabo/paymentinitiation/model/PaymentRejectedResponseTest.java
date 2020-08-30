package com.rabo.paymentinitiation.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentRejectedResponseTest {

    private PaymentRejectedResponse paymentRejectedResponse;

    @Before
    public void init(){
        paymentRejectedResponse = new PaymentRejectedResponse();
        paymentRejectedResponse.setReason("1");
        paymentRejectedResponse.setStatus(TransactionStatus.ACCEPTED);
        paymentRejectedResponse.setReasonCode(ErrorReasonCode.INVALID_REQUEST);
    }

    @Test
    public void whenRequestIsInvalid() {
        assertEquals("1", paymentRejectedResponse.getReason());
        assertEquals("ACCEPTED", paymentRejectedResponse.getStatus().name());
        assertEquals("INVALID_REQUEST", paymentRejectedResponse.getReasonCode().name());
    }

    @Test
    public void whenRequestIsRejected() {
        paymentRejectedResponse.setStatus(TransactionStatus.REJECTED);
        assertEquals("1", paymentRejectedResponse.getReason());
        assertEquals("REJECTED", paymentRejectedResponse.getStatus().name());
        assertEquals("INVALID_REQUEST", paymentRejectedResponse.getReasonCode().name());
    }

    @Test
    public void whenRequestReasonCheck() {
        paymentRejectedResponse.setStatus(TransactionStatus.REJECTED);
        assertEquals("1", paymentRejectedResponse.getReason());
        assertEquals("REJECTED", paymentRejectedResponse.getStatus().name());
        assertEquals("INVALID_REQUEST", paymentRejectedResponse.getReasonCode().name());
    }
    
    @Test
    public void toStringCheck() {
    	assertNotNull(paymentRejectedResponse.toString());
    }
}
