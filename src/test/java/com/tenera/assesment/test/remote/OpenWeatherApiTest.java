package com.tenera.assesment.test.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenera.assesment.remote.impl.OpenMapWeatherInfoProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
public class OpenWeatherApiTest {
    @Autowired
    private OpenMapWeatherInfoProvider openMapWeatherInfoProvider;
    @Test
    @DisplayName("should return success response when valid coordinates passed")
    void testShouldSuccessWhenPassingCorrectCoordinates(){
        String apiResponse =openMapWeatherInfoProvider.getCurrentWeatherInfo("52.5244","13.4105");
        Assertions.assertNotNull(apiResponse);
        Assertions.assertAll(
                ()->{
                    var currentWeatherNode = new ObjectMapper().readTree(apiResponse).get("current");
                    Assertions.assertNotNull(currentWeatherNode.get("pressure"));
                    Assertions.assertNotNull(currentWeatherNode.get("temp"));
                    Assertions.assertNotNull(currentWeatherNode.get("weather").get(0).get("main").textValue());
                }
        );
    }

    @Test
    @DisplayName("should return success response when valid coordinates passed")
    void testShouldFailWhenWrongCoordinatesPassed() throws JsonProcessingException {

        Throwable error =assertThrows(RuntimeException.class,()->openMapWeatherInfoProvider.getCurrentWeatherInfo("123.5244","234.4105"));
        var jsonNode = new ObjectMapper().readTree(error.getMessage());
        Assertions.assertEquals(400,jsonNode.asInt());

    }
}
