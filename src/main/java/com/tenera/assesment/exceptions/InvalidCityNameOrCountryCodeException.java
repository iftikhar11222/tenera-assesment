package com.tenera.assesment.exceptions;

public class InvalidCityNameOrCountryCodeException extends RuntimeException {

    public InvalidCityNameOrCountryCodeException(String message){
        super(message);
    }

}
