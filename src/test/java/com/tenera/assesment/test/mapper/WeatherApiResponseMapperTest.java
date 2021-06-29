package com.tenera.assesment.test.mapper;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;
import com.tenera.assesment.mapper.IRemoteWeatherApiResponseMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

@SpringBootTest
 class WeatherApiResponseMapperTest {

   @Autowired
   private IRemoteWeatherApiResponseMapper apiResponseMapper;
   @ParameterizedTest
   @MethodSource("getJsonDataForCurrentWeatherCall")
   void testShouldSuccessWhenMappingValidCurrentRequestResponse(String currentWeatherDetails){
       var weatherDTO =apiResponseMapper.mapJsonToWeatherDTO(currentWeatherDetails);
       Assertions.assertTrue(weatherDTO.isPresent());
       var dto = weatherDTO.orElseGet(()->new WeatherDTO(-100,-273,false));
       Assertions.assertNotEquals(-273,dto.getTemperature());



   }

    @ParameterizedTest
    @MethodSource("getJsonDataForCurrentWeatherCall")
    void testShouldPassWhenItsRainUmbrellaShouldBeTrue(String currentWeatherDetails){
        var weatherDTO =apiResponseMapper.mapJsonToWeatherDTO(currentWeatherDetails);
        Assertions.assertTrue(weatherDTO.isPresent());
        var dto = weatherDTO.orElseGet(()->new WeatherDTO(-100,-273,false));
        Assertions.assertNotEquals(-273,dto.getTemperature());
        Assertions.assertTrue(dto.isUmbrella());
    }

    @ParameterizedTest
    @MethodSource("getJsonDataForWeatherHistoryCall")
    void testShouldSuccessWhenMappingValidWeatherHistoryRequestResponse(String currentWeatherDetails){
        var historyDTO =apiResponseMapper.mapJsonToWeatherHistoryDTO(currentWeatherDetails);
        Assertions.assertTrue(historyDTO.isPresent());
        var dto = historyDTO.orElseGet(()->new WeatherHistoryDTO(Collections.EMPTY_LIST));
        Assertions.assertNotEquals(0,dto.getAveragePressure());
        Assertions.assertTrue(dto.getHistory().size() == 5);





    }

    private static Stream<Arguments> getJsonDataForWeatherHistoryCall() throws FileNotFoundException {
        var currentWeatherJson = new Scanner
                (new File("historical-weather.json"))
                .useDelimiter("\\Z").next();

        return Stream.of(Arguments.arguments(currentWeatherJson));
    }


    private static Stream<Arguments> getJsonDataForCurrentWeatherCall() throws FileNotFoundException {
        var currentWeatherJson = new Scanner
                (new File("current-weather.json"))
                .useDelimiter("\\Z").next();

        return Stream.of(Arguments.arguments(currentWeatherJson));
    }

}
