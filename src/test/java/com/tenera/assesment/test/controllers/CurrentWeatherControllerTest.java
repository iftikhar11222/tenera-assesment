package com.tenera.assesment.test.controllers;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;
import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
 class CurrentWeatherControllerTest {


    public static final String LOCATION_BERLIN_DE = "Berlin,DE";
    public static final String JSON_FIELD_PRESSURE = "$.pressure";
    public static final String JSON_FIELD_TEMPERATURE = "$.temperature";
    public static final String JSON_PATH_FIELD_UMBRELLA = "$.umbrella";
    public static final String LOCATION_BERLIN_DEU = "Berlin,DEU";
    public static final String JSON_FIELD_MESSAGE = "$.message";
    public static final String INVALID_REQUEST_MESSAGE = "Invalid Request";
    @Autowired private MockMvc mockMvc;
   @MockBean private WeatherService weatherService;

    @Test
    @DisplayName("should return success if valid location is provided in query params")
     void testCurrentWeatherSuccessIfValidLocation() throws Exception {
        var weatherDTO = new WeatherDTO(100,1000,false);
        doReturn(Optional.of(weatherDTO)).when(weatherService).getCurrentWeatherByCity(LOCATION_BERLIN_DE);
       mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,LOCATION_BERLIN_DE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_PRESSURE,is(instanceOf(Integer.class))))
                .andExpect( jsonPath(JSON_FIELD_TEMPERATURE,is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_PATH_FIELD_UMBRELLA,is(instanceOf(Boolean.class))));

        doReturn(Optional.of(weatherDTO)).when(weatherService).getCurrentWeatherByCity(LOCATION_BERLIN_DEU);
        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,LOCATION_BERLIN_DEU)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_PRESSURE,is(instanceOf(Integer.class))))
                .andExpect( jsonPath(JSON_FIELD_TEMPERATURE,is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_PATH_FIELD_UMBRELLA,is(instanceOf(Boolean.class))));

        doReturn(Optional.of(weatherDTO)).when(weatherService).getCurrentWeatherByCity("Berlin , DE");
        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"Berlin , DE")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_PRESSURE,is(instanceOf(Integer.class))))
                .andExpect( jsonPath(JSON_FIELD_TEMPERATURE,is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_PATH_FIELD_UMBRELLA,is(instanceOf(Boolean.class))));
    }



    @Test
    @DisplayName("should fail and return error response if invalid location format")
     void testCurrentWeatherFailureIfInvalidLocationFormat() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"Ber123,DE,,")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));


        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,",DE,,")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));

        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));




    }
    @Test
    @DisplayName("Should fail and return error response if the special character or opposite order")
     void testCurrentWeatherFailureIfInvalidOrder() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"B@rlin,DE")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));


        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"DE,Berlin")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));
    }


    @Test
    @DisplayName("If service return empty or null response should return server error")
     void testCurrentWeatherFailureIfServiceReturnsNull() throws Exception {
        doReturn(Optional.ofNullable(null)).when(weatherService).getCurrentWeatherByCity("Kabul,AF");

        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"Kabul,AF")))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is("System Error")));

    }


}
