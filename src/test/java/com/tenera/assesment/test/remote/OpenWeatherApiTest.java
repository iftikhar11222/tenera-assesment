package com.tenera.assesment.test.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    @DisplayName("current weather api should  success  valid coordinates passed")
    void testCurrentWeatherShouldSuccessWhenPassingCorrectCoordinates(){
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
    @DisplayName("current weather exteral api should fail when invalid coordinates passed")
    void testCurrentWeatherShouldFailWhenWrongCoordinatesPassed() throws JsonProcessingException {

        Throwable error =assertThrows(RuntimeException.class,()->openMapWeatherInfoProvider.getCurrentWeatherInfo("123.5244","234.4105"));
        var jsonNode = new ObjectMapper().readTree(error.getMessage());
        Assertions.assertEquals(400,jsonNode.asInt());

    }
    @Test
    @DisplayName("test weather history external api should return success with valid coordinates")
    void testWeatherHistoryApiShouldSuccessWhenPassingCorrectCoordinates(){
        String apiResponse =openMapWeatherInfoProvider.getHistoricalWeatherInfo("52.5244","13.4105");
        Assertions.assertNotNull(apiResponse);
        Assertions.assertAll(
                ()->{
                    var weatherHistory = new ObjectMapper().readTree(apiResponse).get("daily");
                    Assertions.assertNotNull(weatherHistory.get(0).get("pressure"));
                    Assertions.assertNotNull(weatherHistory.get(0).get("temp"));
                    Assertions.assertNotNull(weatherHistory.get(0).get("weather").get(0).get("main").textValue());
                    Assertions.assertTrue(weatherHistory.get(0).get("temp") instanceof ObjectNode);
                }
        );
    }

    @Test
    @DisplayName("weather history external api should fail when invalid coordinates passed")
    void testWeatherHistoryShouldFailWhenWrongCoordinatesPassed() throws JsonProcessingException {
        Throwable error =assertThrows(RuntimeException.class,()->openMapWeatherInfoProvider.getCurrentWeatherInfo("123.5244","234.4105"));
        var jsonNode = new ObjectMapper().readTree(error.getMessage());
        Assertions.assertEquals(400,jsonNode.asInt());

    }

}
