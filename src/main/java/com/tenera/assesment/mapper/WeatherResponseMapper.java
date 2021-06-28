package com.tenera.assesment.mapper;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;

import java.util.Optional;

public interface WeatherResponseMapper {
    Optional<WeatherDTO> mapJsonToWeatherDTO(String json);

    Optional<WeatherHistoryDTO> mapJsonToWeatherHistoryDTO(String weatherInfoJSON);
}
