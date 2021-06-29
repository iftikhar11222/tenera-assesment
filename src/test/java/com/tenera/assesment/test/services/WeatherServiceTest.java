package com.tenera.assesment.test.services;

import com.tenera.assesment.dto.GeoCodeInfoDTO;
import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;
import com.tenera.assesment.exceptions.InvalidCityNameOrCountryCodeException;
import com.tenera.assesment.mapper.GeoCodingResponseMapper;
import com.tenera.assesment.mapper.IRemoteWeatherApiResponseMapper;
import com.tenera.assesment.remote.CoordinatesProvider;
import com.tenera.assesment.remote.WeatherInfoProvider;
import com.tenera.assesment.service.WeatherService;
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

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
@SpringBootTest
 class WeatherServiceTest {

    @Autowired  private WeatherService weatherService;
    @MockBean private CoordinatesProvider coordinatesProvider;
    @MockBean private WeatherInfoProvider weatherInfoProvider;
    @MockBean private GeoCodingResponseMapper geoCodingResponseMapper;
    @MockBean private IRemoteWeatherApiResponseMapper IRemoteWeatherApiResponseMapper;

    @DisplayName("should return current weather data when valid location is passed")
    @ParameterizedTest
    @MethodSource("getValidJsonDataForCurrentWeather")
    void testTestCurrentWeatherShouldReturnValidResponse(String weatherJson, String locationJson){
        GeoCodeInfoDTO coordinateInfo = GeoCodeInfoDTO.builder().cityName("Berlin").countryCode("DE").build();
        WeatherDTO weatherDTO = new WeatherDTO(1005,100,false);
        doReturn(locationJson).when(coordinatesProvider).getGeocodeInfoByLocation(coordinateInfo);
        doReturn(Optional.of(coordinateInfo)).when(geoCodingResponseMapper).mapJsonToGeoCodingDTO(locationJson);
        doReturn(weatherJson).when(weatherInfoProvider).getCurrentWeatherInfo(coordinateInfo.getLatitude(),coordinateInfo.getLongitude());
        doReturn(Optional.of(weatherDTO)).when(IRemoteWeatherApiResponseMapper).mapJsonToWeatherDTO(weatherJson);
        Optional<WeatherDTO> resultDTO = weatherService.getCurrentWeatherByCity("Berlin,DE");
        assertTrue(resultDTO.isPresent());
        assertSame(weatherDTO,resultDTO.get());
    }


    @DisplayName("should fail if city name is wrong or unknown city name")
    @Test
    void testTestCurrentWeatherShouldFailWhenWrongCityName(){
        var cityNameWrong = "Berliin";
        var countryName = "DE";
        var cityName = "Berlin";
        var countryNameWrong = "ADZ";
        GeoCodeInfoDTO coordinateInfo = GeoCodeInfoDTO.builder().cityName(cityNameWrong).countryCode(countryName).build();
        doThrow(new InvalidCityNameOrCountryCodeException("Wrong City Name or Country Code")).when(coordinatesProvider).getGeocodeInfoByLocation(coordinateInfo);
        assertThrows(InvalidCityNameOrCountryCodeException.class,()->weatherService.getCurrentWeatherByCity(cityNameWrong+","+countryName));
         coordinateInfo = GeoCodeInfoDTO.builder().cityName(cityName).countryCode(countryNameWrong).build();
        doThrow(new InvalidCityNameOrCountryCodeException("Wrong City Name or Country Code")).when(coordinatesProvider).getGeocodeInfoByLocation(coordinateInfo);
        Throwable ex =  assertThrows(InvalidCityNameOrCountryCodeException.class,()->weatherService.getCurrentWeatherByCity(cityName+","+countryNameWrong));
        assertEquals("Wrong City Name or Country Code",ex.getMessage());
    }


    @DisplayName("should return historical weather data when valid location is passed")
    @ParameterizedTest
    @MethodSource("getValidJsonDataForHistoricalWeather")
    void testTestHistoricalWeatherShouldReturnValidResponse(String weatherJson, String locationJson,List<WeatherDTO> weatherDTOList){
        var coordinateInfo = GeoCodeInfoDTO.builder().cityName("Berlin").countryCode("DE").build();
        var weatherDTO = new WeatherHistoryDTO(weatherDTOList);
        doReturn(locationJson).when(coordinatesProvider).getGeocodeInfoByLocation(coordinateInfo);
        doReturn(Optional.of(coordinateInfo)).when(geoCodingResponseMapper).mapJsonToGeoCodingDTO(locationJson);
        doReturn(weatherJson).when(weatherInfoProvider).getHistoricalWeatherInfo(coordinateInfo.getLatitude(),coordinateInfo.getLongitude());
        doReturn(Optional.of(weatherDTO)).when(IRemoteWeatherApiResponseMapper).mapJsonToWeatherHistoryDTO(weatherJson);

        Optional<WeatherHistoryDTO> resultDTO = weatherService.getWeatherHistoryByLocation("Berlin,DE");
        assertTrue(resultDTO.isPresent());
        assertSame(weatherDTO,resultDTO.get());


    }


    @DisplayName("should fail if city name is wrong or unknown city name")
    @Test
    void testTestWeatherHistoryShouldFailWhenWrongCityName(){
        var cityNameWrong = "Berliin";
        var countryName = "DE";
        var cityName = "Berlin";
        var countryNameWrong = "ADZ";
        GeoCodeInfoDTO coordinateInfo = GeoCodeInfoDTO.builder().cityName(cityNameWrong).countryCode(countryName).build();
        doThrow(new InvalidCityNameOrCountryCodeException("Wrong City Name or Country Code")).when(coordinatesProvider).getGeocodeInfoByLocation(coordinateInfo);
        assertThrows(InvalidCityNameOrCountryCodeException.class,()->weatherService.getWeatherHistoryByLocation(cityNameWrong+","+countryName));
        coordinateInfo = GeoCodeInfoDTO.builder().cityName(cityName).countryCode(countryNameWrong).build();
        doThrow(new InvalidCityNameOrCountryCodeException("Wrong City Name or Country Code")).when(coordinatesProvider).getGeocodeInfoByLocation(coordinateInfo);
        Throwable ex =  assertThrows(InvalidCityNameOrCountryCodeException.class,()->weatherService.getWeatherHistoryByLocation(cityName+","+countryNameWrong));
        assertEquals("Wrong City Name or Country Code",ex.getMessage());
    }








    private static Stream<Arguments> getValidJsonDataForCurrentWeather() throws FileNotFoundException {
        var currentWeatherJson = new Scanner
                (new File("current-weather.json"))
                .useDelimiter("\\Z").next();
        var geoCoordinatesJson = new Scanner
                (new File("coordinates.json"))
                .useDelimiter("\\z").next();
        return Stream.of(Arguments.arguments(currentWeatherJson,geoCoordinatesJson));
    }


    public static Stream<Arguments> getValidJsonDataForHistoricalWeather() throws FileNotFoundException{
        var currentWeatherJson = new Scanner
                (new File("historical-weather.json"))
                .useDelimiter("\\Z").next();

          var geoCoordinatesJson = new Scanner
                (new File("coordinates.json"))
                .useDelimiter("\\z").next();
       var weatherDtoList= List.of(new WeatherDTO(1010,90,false),
                new WeatherDTO(1005,85,false),
                new WeatherDTO(1020,95,false),
                new WeatherDTO(1050,105,true),
                new WeatherDTO(1030,105,false));
        return Stream.of(Arguments.arguments(currentWeatherJson,geoCoordinatesJson,weatherDtoList));
    }





}
