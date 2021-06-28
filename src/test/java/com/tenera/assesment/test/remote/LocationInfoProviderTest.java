package com.tenera.assesment.test.remote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenera.assesment.dto.GeoCodeInfoDTO;
import com.tenera.assesment.exceptions.InvalidCityNameOrCountryCodeException;
import com.tenera.assesment.remote.CoordinatesProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class LocationInfoProviderTest {
    @Autowired
    private CoordinatesProvider coordinatesProvider;


    @Test
    @DisplayName("should pass when correct city and country code provided in ISO alpha2")
     void shouldReturnValidResponseIfCorrectCityAndCountryCodeProvided_ISOAlpha2() throws  JsonProcessingException {
        var geoCodeInfoDTO = GeoCodeInfoDTO.builder().countryCode("DE")
                .cityName("Berlin").build();
        String jsonResult =coordinatesProvider.getGeocodeInfoByLocation(geoCodeInfoDTO);
        Assertions.assertNotNull(jsonResult);
        JsonNode locationData = new ObjectMapper().readTree(jsonResult);
        Assertions.assertAll(()-> {
            Assertions.assertNotNull(locationData.get(0).get("lat").asText());
            Assertions.assertNotNull(locationData.get(0).get("lon").asText());
            Assertions.assertNotNull(locationData.get(0).get("name").asText());
            Assertions.assertEquals("52.5244",locationData.get(0).get("lat").asText());
            Assertions.assertEquals("13.4105",locationData.get(0).get("lon").asText());
            Assertions.assertEquals("Berlin",locationData.get(0).get("name").asText());
        } );



     }

    @Test
    @DisplayName("should pass when correct city and country code provided in Iso alpha3")
    void shouldReturnValidResponseIfCorrectCityAndCountryCodeProvided_ISOAlpha3( ) throws JsonProcessingException {
        var geoCodeInfoDTO = GeoCodeInfoDTO.builder().countryCode("DEU")
                .cityName("Berlin").build();

        String jsonResult =coordinatesProvider.getGeocodeInfoByLocation(geoCodeInfoDTO);
        Assertions.assertNotNull(jsonResult);
        JsonNode locationData = new ObjectMapper().readTree(jsonResult);
        Assertions.assertAll(()-> {
            Assertions.assertNotNull(locationData.get(0).get("lat").asText());
            Assertions.assertNotNull(locationData.get(0).get("lon").asText());
            Assertions.assertNotNull(locationData.get(0).get("name").asText());
            Assertions.assertEquals("52.5244",locationData.get(0).get("lat").asText());
            Assertions.assertEquals("13.4105",locationData.get(0).get("lon").asText());
            Assertions.assertEquals("Berlin",locationData.get(0).get("name").asText());
        } );



    }

    @Test
    @DisplayName("should pass when correct city name provided")
    void shouldReturnValidResponseIfCorrectCityNameProvide() throws  JsonProcessingException {
        var geoCodeInfoDTO = GeoCodeInfoDTO.builder()
                .cityName("Berlin").build();
        String jsonResult =coordinatesProvider.getGeocodeInfoByLocation(geoCodeInfoDTO);
        Assertions.assertNotNull(jsonResult);
        JsonNode locationData = new ObjectMapper().readTree(jsonResult);

        Assertions.assertAll(()-> {
            Assertions.assertNotNull(locationData.get(0).get("lat").asText());
            Assertions.assertNotNull(locationData.get(0).get("lon").asText());
            Assertions.assertNotNull(locationData.get(0).get("name").asText());
            Assertions.assertEquals("52.5244",locationData.get(0).get("lat").asText());
            Assertions.assertEquals("13.4105",locationData.get(0).get("lon").asText());
            Assertions.assertEquals("Berlin",locationData.get(0).get("name").asText());
        } );

    }
    @Test
    @DisplayName("should throw exception when city name is missing")
    void shouldFailWhenCityIsEmpty(){
        var geoCodeInfoDTO = GeoCodeInfoDTO.builder()
                .cityName("").countryCode("DE").build();

       Throwable exceptionMessage= assertThrows(InvalidCityNameOrCountryCodeException.class,()->coordinatesProvider.getGeocodeInfoByLocation(geoCodeInfoDTO));
       assertEquals("Wrong/Invalid city name or country code.",exceptionMessage.getMessage());


    }

    @Test
    @DisplayName("should pass when country code is wrong but city is correct ")
    void shouldFailWhenCountryCodeIsWrong() throws JsonProcessingException {
        var geoCodeInfoDTO = GeoCodeInfoDTO.builder()
                .cityName("Berlin").countryCode("GM").build();
        var resultJson =coordinatesProvider.getGeocodeInfoByLocation(geoCodeInfoDTO);
        Assertions.assertNotNull(resultJson);
        JsonNode locationData = new ObjectMapper().readTree(resultJson);

        Assertions.assertAll(()-> {
            Assertions.assertNotNull(locationData.get(0).get("lat").asText());
            Assertions.assertNotNull(locationData.get(0).get("lon").asText());
            Assertions.assertNotNull(locationData.get(0).get("name").asText());
            Assertions.assertEquals("52.5244",locationData.get(0).get("lat").asText());
            Assertions.assertEquals("13.4105",locationData.get(0).get("lon").asText());
            Assertions.assertEquals("Berlin",locationData.get(0).get("name").asText());
        } );
    }




}
