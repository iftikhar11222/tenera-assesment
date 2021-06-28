package com.tenera.assesment.test.mapper;

import com.tenera.assesment.dto.GeoCodeInfoDTO;
import com.tenera.assesment.exceptions.InvalidCityNameOrCountryCodeException;
import com.tenera.assesment.mapper.GeoCodingResponseMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GeoCodingResponseMapperTest {

    @Autowired private GeoCodingResponseMapper responseMapper;

    @ParameterizedTest
    @MethodSource("getGeoCodingJson")
    @DisplayName("should map the json if valid json is provided")
    void testShouldSuccessfullyParseTheJSONResponse(String geoCodingJsonResult){

        Optional<GeoCodeInfoDTO> result =responseMapper.mapJsonToGeoCodingDTO(geoCodingJsonResult);
        Assertions.assertTrue(result.isPresent());
        var dto =result.orElseThrow();
        Assertions.assertAll(()->{
          Assertions.assertNotNull(dto.getLatitude());
            Assertions.assertNotNull(dto.getLongitude());
            Assertions.assertNotNull(dto.getCityName());
            Assertions.assertNotNull(dto.getCountryCode());
        });
        }
    @Test
    @DisplayName("should map the json if valid json is provided")
    void testShouldThrowExceptionForInvalidInput(){
        Throwable exceptionMessage= assertThrows(InvalidCityNameOrCountryCodeException.class,()->responseMapper.mapJsonToGeoCodingDTO("[]"));
        assertEquals("Wrong/Invalid city name or country code.",exceptionMessage.getMessage());

    }

       static Stream<Arguments>  getGeoCodingJson() throws FileNotFoundException {
                 String geoCoordinatesJson = new Scanner
                         (new File("coordinates.json"))
                         .useDelimiter("\\z").next();
                 return Stream.of(Arguments.arguments(geoCoordinatesJson));
             }



}
