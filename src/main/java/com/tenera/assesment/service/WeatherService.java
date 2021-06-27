package com.tenera.assesment.service;

import com.tenera.assesment.dto.WeatherDTO;

import java.util.Optional;

public interface WeatherService {

     WeatherDTO getCurrentWeatherByCity(String location);
}
