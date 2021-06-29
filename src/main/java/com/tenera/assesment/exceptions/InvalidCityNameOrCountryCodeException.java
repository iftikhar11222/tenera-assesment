package com.tenera.assesment.exceptions;

public class InvalidCityNameOrCountryCodeException extends RuntimeException {
    /**
     * Custom exception used when get error about city or country name
     * @param message
     */
    public InvalidCityNameOrCountryCodeException(String message) {
        super(message);
    }

}
