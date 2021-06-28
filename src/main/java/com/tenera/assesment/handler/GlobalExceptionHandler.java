package com.tenera.assesment.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleInvalidRequestException(ConstraintViolationException exception){
        var errorDetails = new ErrorDetails("Invalid Request",400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);

    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> handleInvalidRequestException(RuntimeException exception){
        var errorDetails = new ErrorDetails("System Error",500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);

    }
}
