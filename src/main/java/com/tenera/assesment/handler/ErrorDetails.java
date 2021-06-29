package com.tenera.assesment.handler;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO object return to caller in case there is exception
 * Message hold the cause
 * code hold the http status code
 * @See {@link GlobalExceptionHandler}
 */
public class ErrorDetails {

    private String message;
    private int statusCode;
}
