package com.rabo.paymentinitiation.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;

import com.rabo.paymentinitiation.model.ErrorReasonCode;
import com.rabo.paymentinitiation.model.PaymentRejectedResponse;
import com.rabo.paymentinitiation.model.TransactionStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler
{   
    @ExceptionHandler(ServletRequestBindingException.class)
    public final ResponseEntity<Object> handleHeaderException(Exception exception, WebRequest request)
    {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Bad Request", details);

        return new ResponseEntity<>(error, getRequiredResponseHeaders(request), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> handleConstraintViolation(
                                            ConstraintViolationException exception,
                                            WebRequest request) {
        List<String> details = exception.getConstraintViolations()
                                    .parallelStream()
                                    .map(e -> e.getMessage())
                                    .collect(Collectors.toList());
 
        ErrorResponse error = new ErrorResponse("Bad Request", details);
        return new ResponseEntity<>(error, getRequiredResponseHeaders(request), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception exception, WebRequest request)
    {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        PaymentRejectedResponse paymentRejectedResponse = new PaymentRejectedResponse();
        paymentRejectedResponse.setReason(exception.getLocalizedMessage());
        paymentRejectedResponse.setStatus(TransactionStatus.REJECTED);
        paymentRejectedResponse.setReasonCode(ErrorReasonCode.valueOf(exception.getMessage()));

        return new ResponseEntity<>(paymentRejectedResponse, getRequiredResponseHeaders(request), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders getRequiredResponseHeaders(WebRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-Id", request.getHeader("X-Request-Id"));
        headers.add("Signature-Certificate", request.getHeader("Signature-Certificate"));
        headers.add("Signature", request.getHeader("Signature"));
        System.out.println(headers.toString());
        return headers;
    }
}

