package com.tenera.assesment.service.impl;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;
import com.tenera.assesment.remote.CoordinatesProvider;
import com.tenera.assesment.remote.WeatherInfoProvider;
import com.tenera.assesment.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class WeatherServiceImpl implements WeatherService {

    private CoordinatesProvider coordinatesProvider;
    private WeatherInfoProvider infoProvider;
    @Override
    public Optional<WeatherDTO> getCurrentWeatherByCity(String location) {

        return Optional.empty();
    }

    @Override
    public Optional<WeatherHistoryDTO> getWeatherHistoryByLocation(String location) {
        return Optional.empty();
    }
    @Autowired
    @Lazy
    public void setCoordinatesProvider(CoordinatesProvider coordinatesProvider) {
        this.coordinatesProvider = coordinatesProvider;
    }
    @Autowired
    @Lazy
    public void setInfoProvider(WeatherInfoProvider infoProvider) {
        this.infoProvider = infoProvider;
    }
}
