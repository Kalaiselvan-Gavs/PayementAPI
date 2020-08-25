package com.rabo.paymentinitiation.model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionStatusTest {
	@Test
	public void testEqualStatus() {
		assertEquals("ACCEPTED", TransactionStatus.ACCEPTED.name());
		assertEquals("REJECTED", TransactionStatus.REJECTED.name());
	}
	
	@Test
	public void testUnEqualStatus() {
		assertNotEquals("AACCEPTED", TransactionStatus.ACCEPTED.name());
		assertNotEquals("RREJECTED", TransactionStatus.REJECTED.name());
	}
}
