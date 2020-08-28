package com.rabo.paymentinitiation.filter;

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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class CustomFilterTest {

    @Test(expected = Exception.class)
    public void testDoFilter() throws ServletException, IOException {

    	CustomFilter filter = new CustomFilter();

        HttpServletRequest mockReq = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResp = Mockito.mock(HttpServletResponse.class);
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);
        // mock the getRequestURI() response
        Mockito.when(mockReq.getRequestURI()).thenReturn("/payment/v1.0.0/initiate-payment");

        BufferedReader br = new BufferedReader(new StringReader("test"));
        // mock the getReader() call
        Mockito.when(mockReq.getReader()).thenReturn(br);

        filter.init(mockFilterConfig);
        //filter.doFilter(mockReq, mockResp, mockFilterChain);
        //filter.destroy();
    }

    @Test(expected = Exception.class)
    public void testDoFilterException() throws IOException, ServletException {

    	CustomFilter filter = new CustomFilter();

        HttpServletRequest mockReq = new MockHttpServletRequest();
        HttpServletResponse mockResp = new MockHttpServletResponse();
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);

        filter.init(mockFilterConfig);
        //filter.doFilter(mockReq, mockResp, mockFilterChain);
        //filter.destroy();
    }
} 