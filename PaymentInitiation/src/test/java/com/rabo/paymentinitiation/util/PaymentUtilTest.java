package com.rabo.paymentinitiation.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import com.rabo.paymentinitiation.model.PaymentInitiationRequest;

public class PaymentUtilTest {
	@Test
	public void shoudHandleValidDebtorIBAN() {
		assertEquals(43, PaymentUtil.sumOfDigits("NL02RABO7134384551"));
	}

	@Test
	public void shoudHandleInValidDebtorIBAN() {
		assertEquals(2, PaymentUtil.sumOfDigits("NL02RAB"));
	}

	@Test
	public void shoudHandleInValidDebtorIBAN_WithoutDigits() {
		assertEquals(0, PaymentUtil.sumOfDigits("\t\n\r foo bar \r\n\t"));
	}
	
	@Test
	public void shoudHandleInValidDebtorIBAN_Null() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			PaymentUtil.sumOfDigits(null);
          });
	}
}
