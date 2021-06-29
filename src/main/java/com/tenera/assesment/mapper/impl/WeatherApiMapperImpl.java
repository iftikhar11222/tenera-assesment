package com.tenera.assesment.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;
import com.tenera.assesment.exceptions.ExternalApiException;
import com.tenera.assesment.mapper.ExternalWeatherApiResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class WeatherApiMapperImpl implements ExternalWeatherApiResponseMapper {

    private final Set<String> umbrellaWeathers = Set.of("Rain", "Dizzle", "Thunderstrom");

    @Override
    public Optional<WeatherDTO> mapJsonToWeatherDTO(String apiRawResp) {
        try {
            var currentWeatherData = new ObjectMapper()
                    .readTree(apiRawResp)
                    .get("current");

            return Optional.of(getWeatherDTO(currentWeatherData));
        } catch (JsonProcessingException | NullPointerException e) {
            log.error("error processing", e);
            throw new ExternalApiException(e.getMessage());
        }
    }


    public WeatherDTO getWeatherDTO(JsonNode weatherNode) {
        var currentWeather = new WeatherDTO();
        currentWeather.setPressure(weatherNode.get("pressure").asInt());
        currentWeather.setTemperature(getTemperatureFromNode(weatherNode.get("temp")));
        var weather = weatherNode.get("weather").get(0).get("main").textValue();
        currentWeather.setUmbrella(umbrellaWeathers.contains(weather));
        return currentWeather;
    }

    private int getTemperatureFromNode(JsonNode temperatureNode) {
        if (temperatureNode instanceof ObjectNode) {
            return temperatureNode.get("max").asInt();
        }
        return temperatureNode.asInt();

    }

    @Override
    public Optional<WeatherHistoryDTO> mapJsonToWeatherHistoryDTO(String weatherInfoJSON) {
        try {
            var nodes = new ObjectMapper().readTree(weatherInfoJSON).get("daily");
            var historyDTO = getWeatherHistoryDTO(nodes);
            return Optional.of(historyDTO);
        } catch (JsonProcessingException | NullPointerException e) {
            log.error("error processing", e);
            throw new ExternalApiException(e.getMessage());
        }
    }

    private WeatherHistoryDTO getWeatherHistoryDTO(JsonNode nodes) {
        var listOfDTOs = new ArrayList<WeatherDTO>();
        for (var i = 0; i < 5; i++) {
            listOfDTOs.add(getWeatherDTO(nodes.get(i)));
        }
        return new WeatherHistoryDTO(listOfDTOs);
    }


}
