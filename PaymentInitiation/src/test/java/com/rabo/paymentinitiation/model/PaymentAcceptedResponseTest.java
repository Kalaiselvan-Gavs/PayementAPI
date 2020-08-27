package com.rabo.paymentinitiation.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentAcceptedResponseTest {

    private PaymentAcceptedResponse paymentAcceptedResponse;

    @Before
    public void init(){
        paymentAcceptedResponse = new PaymentAcceptedResponse();
        paymentAcceptedResponse.setPaymentId("1");
        paymentAcceptedResponse.setStatus(TransactionStatus.ACCEPTED);
    }

    @Test
    public void whenPaymentIsAccepted() {
        assertEquals("1", paymentAcceptedResponse.getPaymentId());
        assertEquals("ACCEPTED", paymentAcceptedResponse.getStatus().name());
    }

    @Test
    public void whenPaymentIsRejected() {
        paymentAcceptedResponse.setStatus(TransactionStatus.REJECTED);
        assertEquals("1", paymentAcceptedResponse.getPaymentId());
        assertEquals("REJECTED", paymentAcceptedResponse.getStatus().name());
    }
}
