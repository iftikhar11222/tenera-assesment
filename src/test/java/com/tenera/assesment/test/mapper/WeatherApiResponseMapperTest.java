package com.tenera.assesment.test.mapper;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.mapper.IRemoteWeatherApiResponseMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
 class WeatherApiResponseMapperTest {

   @Autowired
   private IRemoteWeatherApiResponseMapper apiResponseMapper;
   @ParameterizedTest
   @MethodSource("getJsonDataForCurrentWeatherResponse")
   void testShouldSuccessWhenMappingValidCurrentRequestResponse(String currentWeatherDetails){
       Optional<WeatherDTO> weatherDTO =apiResponseMapper.mapJsonToWeatherDTO(currentWeatherDetails);
       Assertions.assertTrue(weatherDTO.isPresent());
   }


}
