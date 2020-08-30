package com.rabo.paymentinitiation.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.rabo.paymentinitiation.service.PaymentService;

public class CustomFilterTest {
	
	@MockBean
    private PaymentService paymentService;
	
    @Test(expected = Exception.class)
    public void testDoFilter() throws Exception {

    	CustomFilter filter = new CustomFilter();

        HttpServletRequest mockReq = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResp = Mockito.mock(HttpServletResponse.class);
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);
        // mock the getRequestURI() response
        Mockito.when(mockReq.getRequestURI()).thenReturn("/payment/v1.0.0/initiate-payment");

        //paymentService.whiteListedCertificatesValidation(any(String.class));
        //verify(paymentService).whiteListedCertificatesValidation(any(String.class));
        //paymentService.verifySignature(any(String.class), any(String.class), any(String.class), any(String.class));

        BufferedReader br = new BufferedReader(new StringReader("test"));
        // mock the getReader() call
        Mockito.when(mockReq.getReader()).thenReturn(br);

        filter.init(mockFilterConfig);
        filter.doFilter(mockReq, mockResp, mockFilterChain);
        filter.destroy();
    }

    @Test(expected = Exception.class)
    public void testDoFilterException() throws Exception {

    	CustomFilter filter = new CustomFilter();

        HttpServletRequest mockReq = new MockHttpServletRequest();
        HttpServletResponse mockResp = new MockHttpServletResponse();
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);

        //paymentService.whiteListedCertificatesValidation(any(String.class));
        //paymentService.verifySignature(any(String.class), any(String.class), any(String.class), any(String.class));
        
        filter.init(mockFilterConfig);
        filter.doFilter(mockReq, mockResp, mockFilterChain);
        filter.destroy();
    }
} 