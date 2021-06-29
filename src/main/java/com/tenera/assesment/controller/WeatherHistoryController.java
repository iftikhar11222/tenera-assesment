package com.tenera.assesment.controller;

import com.tenera.assesment.dto.WeatherHistoryDTO;
import com.tenera.assesment.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.tenera.assesment.controller.ApiConstants.WEATHER_HISTORY_URI;

@RestController
@RequestMapping(ApiConstants.CONTROLLER_BASE_URI + WEATHER_HISTORY_URI)
@Validated
public class WeatherHistoryController {

    private WeatherService weatherService;

    /**
     * ُThe end point serves the request for weather history service
     *
     *
     * @param location
     * validation pattern requires that request should follow the city name followed by optional
     * 2-3 digit country code
     * @return
     */
    @GetMapping("")
    public ResponseEntity<WeatherHistoryDTO> getWeatherHistoryByLocation(@RequestParam(ApiConstants.QUERY_PARAM_LOCATION)
                                                                         @Pattern(regexp = "^[a-zA-Z ]+\\,*[ a-zA-Z]{0,3}$")
                                                                         @NotNull String location
    ) {


        return ResponseEntity.ok().body(
                weatherService.getWeatherHistoryByLocation(location)
                        .orElseThrow(RuntimeException::new));
    }

    @Autowired
    @Lazy
    public void setWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
}
