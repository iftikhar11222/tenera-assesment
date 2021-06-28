package com.tenera.assesment.remote.impl;

import com.tenera.assesment.dto.GeoCodeInfoDTO;
import com.tenera.assesment.exceptions.InvalidCityNameOrCountryCodeException;
import com.tenera.assesment.remote.CoordinatesProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class OpenMapLocationProvider implements CoordinatesProvider {

    @Value("${location.api}")
    private String locationAPIBaseUrl;
    @Value("${api.id}")
    private String apiId;

    private RestTemplate restTemplate;

    Map<String,String> isoCountryCodes;

    @Override
    public String getGeocodeInfoByLocation(GeoCodeInfoDTO info) {

        var countryCode = validateCountryCode(info.getCountryCode());
        if( isEmptyOrNull(info.getCityName())){
            throw new InvalidCityNameOrCountryCodeException("Wrong/Invalid city name or country code.");
        }
        var url = buildLocationUrl(info.getCityName(), countryCode);
        var response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    private String validateCountryCode(String countryCode) {
        if (!isEmptyOrNull(countryCode)) {
        return convertCountryAlpha3ToAlpha2(countryCode);
        }
        return "";
    }

    private String convertCountryAlpha3ToAlpha2(String countryCode) {
        if(countryCode.length()==2) return countryCode;
       return isoCountryCodes.get(countryCode);
    }

    private String buildLocationUrl (String city, String country){
           return UriComponentsBuilder.newInstance()
                    .scheme("http").host(locationAPIBaseUrl).pathSegment()
                    .queryParam("q", city + ",", ",", country)
                    .queryParam("appid", apiId)
                    .build().toUriString();


        }

        @Autowired
        public void setRestTemplate (RestTemplate restTemplate){
            this.restTemplate = restTemplate;
        }


    private boolean isEmptyOrNull(String countryCode) {
        return countryCode==null || countryCode.trim().isEmpty();

    }
    @Autowired
    @Qualifier("isoCountryCodes")
    public void setIsoCountryCodes(Map<String, String> isoCountryCodes) {
        this.isoCountryCodes = isoCountryCodes;
    }
}