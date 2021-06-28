package com.tenera.assesment.test.services;

import com.tenera.assesment.dto.GeoCodeInfoDTO;
import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.mapper.GeoCodingResponseMapper;
import com.tenera.assesment.mapper.WeatherResponseMapper;
import com.tenera.assesment.remote.CoordinatesProvider;
import com.tenera.assesment.remote.WeatherInfoProvider;
import com.tenera.assesment.service.WeatherService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WeatherServiceTest {

    @Autowired  private WeatherService weatherService;
    @MockBean private CoordinatesProvider coordinatesProvider;
    @MockBean private WeatherInfoProvider weatherInfoProvider;
    @MockBean private GeoCodingResponseMapper geoCodingResponseMapper;
    @MockBean private WeatherResponseMapper weatherResponseMapper;

    @DisplayName("should return current weather data when valid location is passed")
    @ParameterizedTest
    @MethodSource("getJsonData")
    void testTestCurrentWeatherShouldReturnValidResponse(String weatherJson, String locationJson){
        GeoCodeInfoDTO coordinateInfo = new GeoCodeInfoDTO("52.5244","52.5244","Berlin","DE");
        WeatherDTO weatherDTO = new WeatherDTO(1005,100,false);
        doReturn(Optional.of(locationJson)).when(coordinatesProvider).getGeocodeInfoByLocation("London,GB");
        doReturn(Optional.of(coordinateInfo)).when(geoCodingResponseMapper).mapJsonToGeoCodingDTO(locationJson);
        doReturn(weatherJson).when(weatherInfoProvider).getCurrentWeatherInfo(coordinateInfo.getLatitude(),coordinateInfo.getLongitude());
        doReturn(Optional.of(weatherDTO)).when(weatherResponseMapper).mapJsonToWeatherDTO(weatherJson);
        Optional<WeatherDTO> resultDTO = weatherService.getCurrentWeatherByCity("Berlin,DE");
         assertTrue(resultDTO.isPresent());
         assertSame(weatherDTO,resultDTO.get());


    }
    private static Stream<Arguments> getJsonData() throws FileNotFoundException {


        String currentWeatherJson = new Scanner
                (new File("current-weather.json"))
                .useDelimiter("\\Z").next();
        String geoCoordinatesJson = new Scanner
                (new File("coordinates.json"))
                .useDelimiter("\\z").next();
        return Stream.of(Arguments.arguments(currentWeatherJson,geoCoordinatesJson));
    }

    /**
     *  private static Stream<Arguments> weatherDTOListProvider(){
     *         return Stream.of(
     *                 Arguments.arguments(List.of(new WeatherDTO(1010,90,false),
     *                         new WeatherDTO(1005,85,false),
     *                         new WeatherDTO(1020,95,false),
     *                         new WeatherDTO(1050,105,true),
     *                         new WeatherDTO(1030,105,false)
     *
     *                         )
     *
     *         ));
     *     }
     *
     *
     *
     */

}
