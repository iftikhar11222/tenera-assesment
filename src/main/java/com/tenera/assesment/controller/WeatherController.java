package com.tenera.assesment.controller;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weather")
public class WeatherController {
    private WeatherService weatherService;

    /**
     *
     * @param location
     * @return ResponseEntity
     */

    @GetMapping("current")
    public ResponseEntity<?> getCurrentWeatherByCity(@RequestParam("location") String location){
        return ResponseEntity.ok()
                .body(weatherService
                        .getCurrentWeatherByCity(location));
    }





    @Autowired
    public void setWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


}
