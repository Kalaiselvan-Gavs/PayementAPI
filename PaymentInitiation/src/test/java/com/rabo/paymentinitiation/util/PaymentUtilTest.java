package com.rabo.paymentinitiation.util;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

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
	
	@Test
	public void shoudHandleFormatCertificateString() {
		String signatureCertificate = Constants.BEGIN_CERTIFICATE +"da+Qmv1MZeHaMFAZvz0OcDnvDwzXOSVOTU1XX" + Constants.END_CERTIFICATE;
		String formatCertificateString = PaymentUtil.formatSignatureCertificate(signatureCertificate);
		assertNotNull(formatCertificateString);
	}
	
	@Test
	public void shouldHandleEmptyFormatCertificateString() {
		
		Assertions.assertThrows(RuntimeException.class, () -> {
			PaymentUtil.formatSignatureCertificate(null);
          });
	}
	
	@Test
	public void shouldGetCurrentTimeStamp() {
		Timestamp currentTimestamp = PaymentUtil.getCurrentTimeStamp();
		assertNotNull(currentTimestamp);
	}
}
