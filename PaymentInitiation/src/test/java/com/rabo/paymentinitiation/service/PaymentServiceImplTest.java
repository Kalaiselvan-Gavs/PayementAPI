package com.rabo.paymentinitiation.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentServiceImplTest {

    @InjectMocks
    PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void whenCorrectsumOfDigits() {
        int sum = 43;
        when(paymentService.sumOfDigits("NL02RABO7134384551")).thenReturn(sum);
        int realSum = paymentService.sumOfDigits("NL02RABO7134384551");
        assertEquals(43, realSum);
    }

    @Test
    void whenInCorrectsumOfDigits() {
        int sum = 42;
        when(paymentService.sumOfDigits("NL02RABO7134384551")).thenReturn(sum);
        int realSum = paymentService.sumOfDigits("NL02RABO7134384551");
        assertEquals(sum, realSum);
    }
}