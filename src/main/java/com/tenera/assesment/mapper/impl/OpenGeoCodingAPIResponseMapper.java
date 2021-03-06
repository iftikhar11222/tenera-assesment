package com.tenera.assesment.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenera.assesment.dto.GeoCodeInfoDTO;
import com.tenera.assesment.exceptions.InvalidCityNameOrCountryCodeException;
import com.tenera.assesment.mapper.LocationAPIResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class OpenGeoCodingAPIResponseMapper implements LocationAPIResponseMapper {
    @Override
    public Optional<GeoCodeInfoDTO> mapJsonToGeoCodingDTO(String locationApiResponse) {
        try {

            var locationData = new ObjectMapper().readTree(locationApiResponse).get(0);
            if (locationData == null) {
                throw new InvalidCityNameOrCountryCodeException("Wrong/Invalid city name or country code.");
            }
            var geoCodingInfo = new GeoCodeInfoDTO();
            geoCodingInfo.setLatitude(locationData.get("lat").asText());
            geoCodingInfo.setLongitude(locationData.get("lon").asText());
            geoCodingInfo.setCityName(locationData.get("name").asText());
            geoCodingInfo.setCountryCode(locationData.get("country").asText());
            return Optional.of(geoCodingInfo);

        } catch (JsonProcessingException | NullPointerException ex) {
            log.error("error parsing the json input", ex);
            throw new InvalidCityNameOrCountryCodeException("Wrong/Invalid city name or country code.");

        }
    }

}
