package com.rabo.paymentinitiation.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
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


}