package com.tenera.assesment.mapper;

import com.tenera.assesment.dto.WeatherDTO;

import java.util.Optional;

public interface WeatherResponseMapper {
    Optional<WeatherDTO> mapJsonToWeatherDTO(String json);
}
