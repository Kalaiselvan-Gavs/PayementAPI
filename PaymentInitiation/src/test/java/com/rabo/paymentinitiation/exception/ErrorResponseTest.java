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
}
