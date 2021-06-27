package com.tenera.assesment.service;

import com.tenera.assesment.dto.WeatherDTO;

import java.util.Optional;

public interface WeatherService {

     Optional<WeatherDTO> getCurrentWeatherByCity(String location);
}
