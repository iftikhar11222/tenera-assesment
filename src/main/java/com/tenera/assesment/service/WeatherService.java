package com.tenera.assesment.service;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;

import java.util.Optional;

public interface WeatherService {

     Optional<WeatherDTO> getCurrentWeatherByCity(String location);

    Optional<WeatherHistoryDTO> getWeatherHistoryByLocation(String location);
}
