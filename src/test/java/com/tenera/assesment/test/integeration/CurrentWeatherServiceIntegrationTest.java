package com.tenera.assesment.test.integeration;

import com.tenera.assesment.test.controllers.ApiConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CurrentWeatherServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    public static final String LOCATION_BERLIN_DE = "Berlin,DE";
    public static final String JSON_FIELD_PRESSURE = "$.pressure";
    public static final String JSON_FIELD_TEMPERATURE = "$.temperature";
    public static final String JSON_PATH_FIELD_UMBRELLA = "$.umbrella";
    public static final String LOCATION_BERLIN_DEU = "Berlin,DEU";
    public static final String JSON_FIELD_MESSAGE = "$.message";
    public static final String INVALID_REQUEST_MESSAGE = "Invalid Request";

    @Test
    @DisplayName("Should pass and return valid response if passed valid params to current weather service")
    void testCurrentWeatherServiceShouldSuccessWhenCorrectParamsPassed() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI, LOCATION_BERLIN_DE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_PRESSURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_FIELD_TEMPERATURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_PATH_FIELD_UMBRELLA, is(instanceOf(Boolean.class))));


    }

    @Test
    @DisplayName("Should pass and return valid response if passed valid params to current weather service")
    void testShouldPassWhenValidParamsWithExtraSpace() throws Exception {

        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI, "Berlin , DE")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_PRESSURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_FIELD_TEMPERATURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_PATH_FIELD_UMBRELLA, is(instanceOf(Boolean.class))));
    }

    @Test
    @DisplayName("should pass when valid 3 digit country code is passed to current weather service")
    void testShouldPassWhenCountryCodeIsISO_3ToCurrentWeather() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI, LOCATION_BERLIN_DEU)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_PRESSURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_FIELD_TEMPERATURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_PATH_FIELD_UMBRELLA, is(instanceOf(Boolean.class))));
    }


    @Test
    @DisplayName("should fail when invalid response is passed to current weather service")
    void testShouldFailWhenInvalidParamsPassedToCurrentWeatherService() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI, "Ber123,DE,,")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE, is(INVALID_REQUEST_MESSAGE)));

    }
    @Test
    @DisplayName("Should fail when params passed in reverse order to current weather service")
    void testShouldFailWhenValidCityNameAndCountryCodeButReverseOrderToCurrentWeather() throws Exception {

        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"DE,Berlin")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));
    }

    @Test
    @DisplayName("Should fail when special character  in query params to current weather service")
    void testShouldFailWhenSpecialCharacterInParamsToCurrentWeatherService() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"B@rlin,DE")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));
    }




}