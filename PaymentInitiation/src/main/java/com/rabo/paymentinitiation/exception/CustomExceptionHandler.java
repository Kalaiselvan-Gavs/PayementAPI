package com.rabo.paymentinitiation.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.rabo.paymentinitiation.model.ErrorReasonCode;
import com.rabo.paymentinitiation.model.PaymentRejectedResponse;
import com.rabo.paymentinitiation.model.TransactionStatus;
import com.rabo.paymentinitiation.util.Constants;
import com.rabo.paymentinitiation.util.PaymentUtil;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

//@ControllerAdvice
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler
{   
    @ExceptionHandler(ServletRequestBindingException.class)
    public final ResponseEntity<Object> handleHeaderException(Exception exception, WebRequest request)
    {
    	List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        
        String timeStamp = PaymentUtil.getCurrentTimeStamp().toString();
        ErrorResponse error = new ErrorResponse(timeStamp, HttpStatus.BAD_REQUEST.value(), ErrorReasonCode.INVALID_REQUEST.getValue(), details);
        return new ResponseEntity<>(error, getRequiredResponseHeaders(request), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolation(MethodArgumentNotValidException exception, WebRequest request) {
        
    	List<String> details = exception.getBindingResult().getAllErrors().stream().map(e -> e.getDefaultMessage())
				.collect(Collectors.toList());
    	
    	String timeStamp = PaymentUtil.getCurrentTimeStamp().toString();
        ErrorResponse error = new ErrorResponse(timeStamp, HttpStatus.BAD_REQUEST.value(), ErrorReasonCode.INVALID_REQUEST.getValue(), details);
        return new ResponseEntity<>(error, getRequiredResponseHeaders(request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidException.class)
    public final ResponseEntity<Object> handleInvalidException(InvalidException exception, WebRequest request)
    {
    	PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();
        paymentRejectedResponse.setReason(ErrorReasonCode.valueOf(exception.getLocalizedMessage()).getValue());
        paymentRejectedResponse.setStatus(TransactionStatus.REJECTED);
        paymentRejectedResponse.setReasonCode(ErrorReasonCode.valueOf(exception.getLocalizedMessage()));

        return new ResponseEntity<>(paymentRejectedResponse, getRequiredResponseHeaders(request), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(GeneralException.class)
    public final ResponseEntity<Object> handleGeneralException(GeneralException exception, WebRequest request)
    {
    	PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();
        paymentRejectedResponse.setReason(ErrorReasonCode.valueOf(exception.getLocalizedMessage()).getValue());
        paymentRejectedResponse.setStatus(TransactionStatus.REJECTED);
        paymentRejectedResponse.setReasonCode(ErrorReasonCode.valueOf(exception.getLocalizedMessage()));
    	
        return new ResponseEntity<>(paymentRejectedResponse, getRequiredResponseHeaders(request), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    private HttpHeaders getRequiredResponseHeaders(WebRequest request) 
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.X_REQUEST_ID, request.getHeader(Constants.X_REQUEST_ID));
        headers.add(Constants.SIGNATURE_CERTIFICATE, request.getHeader(Constants.SIGNATURE_CERTIFICATE));
        headers.add(Constants.SIGNATURE, request.getHeader(Constants.SIGNATURE));
        return headers;
    }
}

