package com.tenera.assesment.service.impl;

import com.tenera.assesment.dto.GeoCodeInfoDTO;
import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;
import com.tenera.assesment.exceptions.InvalidCityNameOrCountryCodeException;
import com.tenera.assesment.mapper.LocationAPIResponseMapper;
import com.tenera.assesment.mapper.ExternalWeatherApiResponseMapper;
import com.tenera.assesment.external.LocationProvider;
import com.tenera.assesment.external.WeatherInfoProvider;
import com.tenera.assesment.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private LocationProvider locationProvider;
    private WeatherInfoProvider weatherInfoProvider;
    private LocationAPIResponseMapper geoCodingResponseMapper;
    private ExternalWeatherApiResponseMapper externalWeatherApiResponseMapper;

    @Override
    public Optional<WeatherDTO> getCurrentWeatherByCity(String location) {

        var geoCodeInfoJSON = locationProvider.getGeocodeInfoByLocation(getCityNameAndCountryCode(location));
        var geoCodeInfoDTO =
                geoCodingResponseMapper.
                        mapJsonToGeoCodingDTO(geoCodeInfoJSON)
                        .orElseThrow(() -> new InvalidCityNameOrCountryCodeException("Invalid City Name or Country Code "));

        var weatherInfoJSON = weatherInfoProvider.getCurrentWeatherInfo(geoCodeInfoDTO.getLatitude(), geoCodeInfoDTO.getLongitude());
        return externalWeatherApiResponseMapper.mapJsonToWeatherDTO(weatherInfoJSON);
    }


    @Override
    public Optional<WeatherHistoryDTO> getWeatherHistoryByLocation(String location) {
        var geoCodeInfoJSON = locationProvider.getGeocodeInfoByLocation(getCityNameAndCountryCode(location));
        var geoCodeInfoDTO = geoCodingResponseMapper.mapJsonToGeoCodingDTO(geoCodeInfoJSON)
                .orElseThrow(() -> new InvalidCityNameOrCountryCodeException
                        ("Invalid City name or Country Code"));
        var weatherInfoJSON = weatherInfoProvider.getHistoricalWeatherInfo(geoCodeInfoDTO.getLatitude(), geoCodeInfoDTO.getLongitude());
        return externalWeatherApiResponseMapper.mapJsonToWeatherHistoryDTO(weatherInfoJSON);
    }


    @Autowired
    @Lazy
    public void setCoordinatesProvider(LocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    @Autowired
    @Lazy
    public void setGeoCodingResponseMapper(LocationAPIResponseMapper geoCodingResponseMapper) {
        this.geoCodingResponseMapper = geoCodingResponseMapper;
    }

    @Autowired
    @Lazy
    public void setWeatherInfoProvider(WeatherInfoProvider weatherInfoProvider) {
        this.weatherInfoProvider = weatherInfoProvider;
    }

    @Autowired
    @Lazy
    public void setWeatherResponseMapper(ExternalWeatherApiResponseMapper ExternalWeatherApiResponseMapper) {
        this.externalWeatherApiResponseMapper = ExternalWeatherApiResponseMapper;
    }

    private GeoCodeInfoDTO getCityNameAndCountryCode(String location) {
        var locationSplit = location.split(",");
        var geoCodeInfoDTO = new GeoCodeInfoDTO();
        geoCodeInfoDTO.setCityName(locationSplit[0].trim());
        if (locationSplit.length > 1) {
            geoCodeInfoDTO.setCountryCode(locationSplit[1].trim());
        }
        return geoCodeInfoDTO;
    }
}
