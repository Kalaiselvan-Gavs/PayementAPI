package com.rabo.paymentinitiation.exception;

import static org.junit.Assert.assertEquals;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabo.paymentinitiation.util.PaymentUtil;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ErrorResponseTest {

	ErrorResponse errorResponse;
	
	@Before
    public void init() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		errorResponse = new ErrorResponse("Error");	
		List<String> list = new ArrayList<>();
		list.add("ErrorItem");
		errorResponse.setDetails(list);
    }
	
	@Test
	public void whenResponseNameCheck() {
		String message = errorResponse.getMessage();
		assertEquals("Error", message);
	}
	
	@Test
	public void whenResponseListItemCheck() {
		String message = errorResponse.getDetails().get(0);
		assertEquals("ErrorItem", message);
	}
	
	@Test
	public void whenResponseListCheck() {
		List<String> list = new ArrayList<>();
		list.add("ErrorItem");
		errorResponse = new ErrorResponse("Error",list);
		assertEquals("Error", errorResponse.getMessage());
		assertEquals("ErrorItem", errorResponse.getDetails().get(0));
	}
	
	@Test
	public void whenSetResponseCheck() {
		errorResponse = new ErrorResponse("Error");	
		errorResponse.setMessage("ErrorItem");
		assertEquals("ErrorItem", errorResponse.getMessage());
	}
	
	@Test
	public void whenSetErrorCheck() {
		errorResponse = new ErrorResponse("Error");	
		errorResponse.setError("Error");
		assertEquals("Error", errorResponse.getError());
	}
	
	@Test
	public void whenSetStatusCheck() {
		errorResponse = new ErrorResponse("Error");	
		errorResponse.setStatus(400);
		assertEquals(400, errorResponse.getStatus());
	}
	
	@Test
	public void whenSetTimestampCheck() {
		errorResponse = new ErrorResponse("Error");
		String timeStamp = PaymentUtil.getCurrentTimeStamp().toString();
		errorResponse.setTimeStamp(timeStamp);
		assertEquals(timeStamp, errorResponse.getTimeStamp());
	}
}
