package com.rabo.paymentinitiation.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErrorReasonCodeTest {
	@Test
	public void testEqualError() {
		assertEquals("GENERAL_ERROR", ErrorReasonCode.GENERAL_ERROR.name());
		assertEquals("INVALID_REQUEST", ErrorReasonCode.INVALID_REQUEST.name());
		assertEquals("INVALID_SIGNATURE", ErrorReasonCode.INVALID_SIGNATURE.name());
		assertEquals("LIMIT_EXCEEDED", ErrorReasonCode.LIMIT_EXCEEDED.name());
		assertEquals("UNKNOWN_CERTIFICATE", ErrorReasonCode.UNKNOWN_CERTIFICATE.name());
	}
	
	@Test
	public void testUnEqualError() {
		assertNotEquals("GENERAL__ERROR", ErrorReasonCode.GENERAL_ERROR.name());
		assertNotEquals("INVALID__REQUEST", ErrorReasonCode.INVALID_REQUEST.name());
		assertNotEquals("INVALID__SIGNATURE", ErrorReasonCode.INVALID_SIGNATURE.name());
		assertNotEquals("LIMIT__EXCEEDED", ErrorReasonCode.LIMIT_EXCEEDED.name());
		assertNotEquals("UNKNOWN__CERTIFICATE", ErrorReasonCode.UNKNOWN_CERTIFICATE.name());

	}
}
