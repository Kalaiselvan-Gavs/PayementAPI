package com.rabo.paymentinitiation.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.service.PaymentServiceImpl;
import com.rabo.paymentinitiation.util.Constants;

//import com.in28minutes.springboot.model.Course;
//import com.in28minutes.springboot.service.StudentService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PaymentInitiationController.class)
//@WithMockUser
public class PaymentInitiationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PaymentService paymentService;

	private ObjectMapper objectMappaer;
    
    private PaymentInitiationRequest paymentRequest;
   
    
    @Before
    public void init() {
    	this.mockMvc = MockMvcBuilders.standaloneSetup(new PaymentInitiationController()).build();
    	objectMappaer = new ObjectMapper();
    	paymentRequest = new PaymentInitiationRequest();
    	paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");
        //when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(false);
    }
    
	@Test
	public void retrieveDetailsForCourse() throws Exception {

		Mockito.when(paymentService.checkForAmoutLimitExceeded(Mockito.anyObject())).thenReturn(false);
		//when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);

		
		//when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		//assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		
		/*RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/students/Student1/courses/Course1").accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println(result.getResponse());
		String expected = "{id:Course1,name:Spring,description:10Steps}";

		// {"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);*/
	}

}

/*
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.paymentinitiation.model.PaymentInitiationRequest;
import com.rabo.paymentinitiation.service.PaymentService;
import com.rabo.paymentinitiation.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentInitiationControllerTest {

    private ObjectMapper objectMappaer;
    
    private PaymentInitiationRequest paymentRequest;
    
    @InjectMocks
    PaymentInitiationController paymentInitiationController;

    @Autowired
    private MockMvc mockMvc;
    
    @Mock
    private PaymentService paymentService;

    
    @Before
    public void init() throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
    	this.mockMvc = MockMvcBuilders.standaloneSetup(new PaymentInitiationController()).build();
    	objectMappaer = new ObjectMapper();
    	paymentRequest = new PaymentInitiationRequest();
    	paymentRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentRequest.setAmount("1.0");
        paymentRequest.setEndToEndId("1");
        //when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(false);
        
    }

    @Test
    public void whenCreatePayment() throws Exception {
    	when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(false);
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    
    @Test
    public void whenaymentwithAmountExceeded() throws Exception {
    	when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    
    @Test
    public void whenPaymentwithReequestEmpty() throws Exception {
    	when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    
    @Test
    public void whenPayment_AmountExceeded() throws Exception {

    	paymentRequest.setDebtorIBAN("NL02RABO0222222222");
    	when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	
    }
    
    @Test
    public void whenPaymentAmountExceededWithZeroAmt() throws Exception {
    	when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
    	paymentRequest.setDebtorIBAN("NL02RABO0222222222");
    	paymentRequest.setAmount("0");
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = null;
	 	Assertions.assertThrows(NullPointerException.class, () -> {
	 		mockMvc.perform(requestBuilder).andReturn();
	 	  });
	 	
	
	 	/*MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());*/
	
    //}
    
    /*@Test
    public void create_Payment_AmountNotExceeded() throws Exception {

    	paymentRequest.setAmount("0");
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment1")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	
    }
    */
    /*@Test
    public void create_Payment_SignaureVerified() throws Exception {

    	boolean isVerified = true;
    	
    	when(paymentService.verifySignature(any(String.class), any(String.class))).thenReturn(isVerified);

    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	
    }*/
    
    /*@Test
    public void create_Payment_SignaureNotVerified() throws Exception {

    	boolean isVerified = false;
    	
    	when(paymentService.verifySignature(any(String.class), any(String.class))).thenReturn(isVerified);

    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	
    }*/
    /*
    @Test
    public void whenDebtorIBANNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setDebtorIBAN(null);
        
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	
    }
    
    @Test
    public void whenCreditorIBANNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setCreditorIBAN(null);
        
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	
    }
    
    @Test
    public void whenAmountNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setAmount(null);
        
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	
    }
    
    @Test
    public void whenEndtoEndIdNullValue_thenReturns400Error() throws Exception {
    	paymentRequest.setEndToEndId(null);
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }
    
    @Test
    public void whenRequestId_Header_Missing_thenReturns400Error() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		//.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2")
				.header(Constants.SIGNATURE, "3");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        
    }
    
    @Test
    public void whenSignature_Header_Missing_thenReturns400Error() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
 				.post("/payment/v1.0.0/initiate-payment")
 				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
 				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        		.header(Constants.X_REQUEST_ID, "1")
        		.header(Constants.SIGNATURE_CERTIFICATE, "2");
     
	 	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	
		MockHttpServletResponse response = result.getResponse();
	
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        
    }
    
    @Test
    public void whenSignatureCer_Header_Missing_thenReturns400Error() throws Exception {
    	Mockito.when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
     				.post("/payment/v1.0.0/initiate-payment")
     				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
     				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
     				.header(Constants.SIGNATURE_CERTIFICATE, "2")
            		.header(Constants.X_REQUEST_ID, "1")
    				.header(Constants.SIGNATURE, "3");
         
     	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());	
    }

    @Test
    public void whensignatureDetails_Header_Missing_thenReturns400Error() throws Exception {
    	Mockito.when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
     				.post("/payment/v1.0.0/initiate-payment")
     				.accept(MediaType.APPLICATION_JSON).content(objectMappaer.writeValueAsString(paymentRequest))
     				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
     				.header(Constants.X_REQUEST_ID, "1");
         
     	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());	
    }
    
    @Test
    public void whenContentType_Missing_thenReturns400Error() throws Exception {
    	Mockito.when(paymentService.checkForAmoutLimitExceeded(paymentRequest)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
     				.post("/payment/v1.0.0/initiate-payment")
     				//.accept(MediaType.APPLICATION_JSON)
     				.content(objectMappaer.writeValueAsString(paymentRequest))
     				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
     				.header(Constants.X_REQUEST_ID, "1");
         
     	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());	
    }
    
    @Test
    public void whenInvoke_Invalid_thenReturns400Error() throws Exception {
    	RequestBuilder requestBuilder = MockMvcRequestBuilders
     				.post("/payment/v1.0.0/initiate-payment1")
     				.accept(MediaType.APPLICATION_JSON)
     				.content(objectMappaer.writeValueAsString(paymentRequest))
     				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
     				.header(Constants.SIGNATURE_CERTIFICATE, "2")
            		.header(Constants.X_REQUEST_ID, "1")
    				.header(Constants.SIGNATURE, "3");
         
     	MvcResult result = mockMvc.perform(requestBuilder).andReturn();

    	MockHttpServletResponse response = result.getResponse();

    	assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());	
    }*/
    
//}