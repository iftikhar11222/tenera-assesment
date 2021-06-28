package com.tenera.assesment.service.impl;

import com.tenera.assesment.dto.GeoCodeInfoDTO;
import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;
import com.tenera.assesment.exceptions.InvalidCityNameOrCountryCode;
import com.tenera.assesment.mapper.GeoCodingResponseMapper;
import com.tenera.assesment.mapper.WeatherResponseMapper;
import com.tenera.assesment.remote.CoordinatesProvider;
import com.tenera.assesment.remote.WeatherInfoProvider;
import com.tenera.assesment.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private CoordinatesProvider coordinatesProvider;
    private WeatherInfoProvider weatherInfoProvider;
    private GeoCodingResponseMapper geoCodingResponseMapper;
    private WeatherResponseMapper weatherResponseMapper ;

    @Override
    public Optional<WeatherDTO> getCurrentWeatherByCity(String location) {

       var geoCodeInfoJSON  =coordinatesProvider.getGeocodeInfoByLocation(getCityNameAndCountryCode(location));
       var geoCodeInfoDTO =
               geoCodingResponseMapper.
                       mapJsonToGeoCodingDTO(geoCodeInfoJSON)
                       .orElseThrow(()->new InvalidCityNameOrCountryCode("Invalid City Name or Country Code "));

       var weatherInfoJSON= weatherInfoProvider.getCurrentWeatherInfo(geoCodeInfoDTO.getLatitude(),geoCodeInfoDTO.getLongitude());
       return weatherResponseMapper.mapJsonToWeatherDTO(weatherInfoJSON);
    }







    @Override
    public Optional<WeatherHistoryDTO> getWeatherHistoryByLocation(String location) {
        var geoCodeInfoJSON  =coordinatesProvider.getGeocodeInfoByLocation(getCityNameAndCountryCode(location));
        var geoCodeInfoDTO =geoCodingResponseMapper.mapJsonToGeoCodingDTO(geoCodeInfoJSON)
                .orElseThrow(()->new InvalidCityNameOrCountryCode
                        ("Invalid City name or Country Code"));
        var weatherInfoJSON= weatherInfoProvider.getHistoricalWeatherInfo(geoCodeInfoDTO.getLatitude(),geoCodeInfoDTO.getLongitude());
        return weatherResponseMapper.mapJsonToWeatherHistoryDTO(weatherInfoJSON);
    }


    @Autowired
    @Lazy
    public void setCoordinatesProvider(CoordinatesProvider coordinatesProvider) {
        this.coordinatesProvider = coordinatesProvider;
    }

    @Autowired
    @Lazy
    public void setGeoCodingResponseMapper(GeoCodingResponseMapper geoCodingResponseMapper) {
        this.geoCodingResponseMapper = geoCodingResponseMapper;
    }

    @Autowired
    @Lazy
    public void setWeatherInfoProvider(WeatherInfoProvider weatherInfoProvider) {
        this.weatherInfoProvider = weatherInfoProvider;
    }
    @Autowired
    @Lazy
    public void setWeatherResponseMapper(WeatherResponseMapper weatherResponseMapper) {
        this.weatherResponseMapper = weatherResponseMapper;
    }

    private GeoCodeInfoDTO getCityNameAndCountryCode(String location) {
        var locationSplit = location.split(",");
        var geoCodeInfoDTO = new GeoCodeInfoDTO();
        geoCodeInfoDTO.setCityName(locationSplit[0]);
        if(locationSplit.length > 1){
            geoCodeInfoDTO.setCountryCode(locationSplit[1]);
        }
        return geoCodeInfoDTO;
    }
}
