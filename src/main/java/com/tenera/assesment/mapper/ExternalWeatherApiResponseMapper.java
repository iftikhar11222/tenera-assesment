package com.tenera.assesment.mapper;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;

import java.util.Optional;

/**
 * interface define the method signature to map the raw json
 * response to dto
 * @see {@link com.tenera.assesment.mapper.impl.WeatherApiMapperImpl}
 * for implementation
 */
public interface ExternalWeatherApiResponseMapper {
    Optional<WeatherDTO> mapJsonToWeatherDTO(String json);

    Optional<WeatherHistoryDTO> mapJsonToWeatherHistoryDTO(String weatherInfoJSON);
}
