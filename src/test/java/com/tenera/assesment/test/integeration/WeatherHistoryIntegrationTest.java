package com.tenera.assesment.test.integeration;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;
import com.tenera.assesment.service.WeatherService;
import com.tenera.assesment.test.controllers.ApiConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class WeatherHistoryIntegrationTest {



    @Autowired
    private MockMvc mockMvc;

    public static final String LOCATION_BERLIN_DE = "Berlin,DE";
    public static final String INVALID_REQUEST_MESSAGE = "Invalid Request";
    private static final String JSON_FIELD_AVERAGE_TEMPERATURE = "$.average_temperature";
    private static final String JSON_FIELD_AVERAGE_PRESSURE = "$.average_pressure";
    private static final String $_HISTORY = "$.history";
    public static final String JSON_FIELD_MESSAGE = "$.message";


    @DisplayName("Should return success response when valid location given")
    @Test
    void testWeatherHistorySuccessWhenValidLocation() throws Exception {


        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI, LOCATION_BERLIN_DE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_TEMPERATURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_PRESSURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.history", is(instanceOf(List.class))));

    }
        @Test
        @DisplayName("should return success when 3 digit correct country code is passed")
       void testSuccessWhen3DigitCountryCodeToHistoryService() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI, "Berlin,DEU")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_TEMPERATURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_PRESSURE, is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.history", is(instanceOf(List.class))));
}





        @Test
        @DisplayName("should return success when correct city is passed no country code")
        void testSuccessWhenOnlyCityNameToHistoryService() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,"Berlin")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_TEMPERATURE,is(instanceOf(Integer.class))))
                .andExpect( jsonPath(JSON_FIELD_AVERAGE_PRESSURE,is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.history",is(instanceOf(List.class))));

    }






    @Test
    @DisplayName("should fail and return error response if invalid location format")
    void testWeatherHistoryFailureIfInvalidLocationFormat() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI, "Ber123,DE,,")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE, is(INVALID_REQUEST_MESSAGE)));

    }
    @Test
    @DisplayName("should fail and return when no city name passed")
    void shouldFailWhenNoCityName() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI, ",DE,,")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE, is(INVALID_REQUEST_MESSAGE)));
    }
    @Test
    @DisplayName("should fail and return when no city name passed")
    public void testShouldFailWhenEmptyParams() throws Exception {

    mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"")))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));

}



    @Test
    @DisplayName("Should fail and return error response if the special character or opposite order")
    void testWeatherHistoryFailureIfInvalidOrder() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,"B@rlin,DE")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));


        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"DE,Berlin")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));
    }





    }

