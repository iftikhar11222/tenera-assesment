package com.tenera.assesment.exceptions;

public class InvalidCityNameOrCountryCode extends RuntimeException {

    public InvalidCityNameOrCountryCode(String message){
        super(message);
    }

}
