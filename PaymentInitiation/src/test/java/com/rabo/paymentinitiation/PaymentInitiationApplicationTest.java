package com.rabo.paymentinitiation;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabo.paymentinitiation.controller.PaymentInitiationController;

@RunWith(SpringRunner.class)
@SpringBootTest
class PaymentInitiationApplicationTest {

	@Autowired
	private PaymentInitiationController paymentInitiationController;
	
	/**
     * Test App load without throwing an exception.
     */
    @Test
    public void contextLoads() {
    	assertThat(paymentInitiationController).isNotNull();
    }

    @Test
    public void applicationStarts() throws Exception {
    	PaymentInitiationApplication.main(new String[] {});
    	assertThat(paymentInitiationController).isNotNull();
    }
}