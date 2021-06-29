package com.tenera.assesment.exceptions;

/**
 *
 * Custom exception if get error response from external service
 */
public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String message) {
        super(message);
    }
}
