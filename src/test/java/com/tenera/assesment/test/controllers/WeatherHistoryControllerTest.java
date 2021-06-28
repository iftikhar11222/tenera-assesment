package com.tenera.assesment.test.controllers;

import com.tenera.assesment.dto.WeatherDTO;
import com.tenera.assesment.dto.WeatherHistoryDTO;
import com.tenera.assesment.service.WeatherService;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
 class WeatherHistoryControllerTest {
    public static final String LOCATION_BERLIN_DE = "Berlin,DE";
    public static final String INVALID_REQUEST_MESSAGE = "Invalid Request";
    private static final String JSON_FIELD_AVERAGE_TEMPERATURE = "$.average_temperature";
    private static final String JSON_FIELD_AVERAGE_PRESSURE = "$.average_pressure";
    private static final String $_HISTORY = "$.history";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WeatherService weatherService;
    public static final String JSON_FIELD_MESSAGE = "$.message";

    @DisplayName("Should return success response when valid location given")
    @ParameterizedTest
    @MethodSource("weatherDTOListProvider")
    void testWeatherHistorySuccessWhenValidLocation(List<WeatherDTO> weatherDTOList) throws Exception {

        var weatherHistoryDTO = new WeatherHistoryDTO(weatherDTOList);
        doReturn(Optional.of(weatherHistoryDTO)).when(weatherService).getWeatherHistoryByLocation(LOCATION_BERLIN_DE);

        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,LOCATION_BERLIN_DE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_TEMPERATURE,is(instanceOf(Integer.class))))
                .andExpect( jsonPath(JSON_FIELD_AVERAGE_PRESSURE,is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.history",is(instanceOf(List.class))));


        doReturn(Optional.of(weatherHistoryDTO)).when(weatherService).getWeatherHistoryByLocation("Berlin,DEU");
        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,"Berlin,DEU")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_TEMPERATURE,is(instanceOf(Integer.class))))
                .andExpect( jsonPath(JSON_FIELD_AVERAGE_PRESSURE,is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.history",is(instanceOf(List.class))));


        doReturn(Optional.of(weatherHistoryDTO)).when(weatherService).getWeatherHistoryByLocation("Berlin , DE");

        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,"Berlin , DE")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_TEMPERATURE,is(instanceOf(Integer.class))))
                .andExpect( jsonPath(JSON_FIELD_AVERAGE_PRESSURE,is(instanceOf(Integer.class))))
                .andExpect(jsonPath($_HISTORY,is(instanceOf(List.class))));

        doReturn(Optional.of(weatherHistoryDTO)).when(weatherService).getWeatherHistoryByLocation("Berlin");
        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,"Berlin")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_TEMPERATURE,is(instanceOf(Integer.class))))
                .andExpect( jsonPath(JSON_FIELD_AVERAGE_PRESSURE,is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.history",is(instanceOf(List.class))));

    }

    @DisplayName("Should show correct average temp and average pressure")
    @ParameterizedTest
    @MethodSource("weatherDTOListProvider")
     void testAverageTempAndPressureFromHistory(List<WeatherDTO> weatherDTOList) throws Exception {
        var weatherHistoryDTO = new WeatherHistoryDTO(weatherDTOList);
        doReturn(Optional.of(weatherHistoryDTO)).when(weatherService).getWeatherHistoryByLocation(LOCATION_BERLIN_DE);
        var averageTemp = (weatherDTOList.stream().map(item->item.getTemperature()).reduce(0,Integer::sum))/weatherDTOList.size();
        var averagePressure = (weatherDTOList.stream().map(item->item.getPressure()).reduce(0,Integer::sum))/weatherDTOList.size();
        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,LOCATION_BERLIN_DE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_AVERAGE_TEMPERATURE,is(averageTemp)))
                .andExpect( jsonPath(JSON_FIELD_AVERAGE_PRESSURE,is(averagePressure)));
    }




    @Test
    @DisplayName("should fail and return error response if invalid location format")
    void testWeatherHistoryFailureIfInvalidLocationFormat() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,"Ber123,DE,,")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is(INVALID_REQUEST_MESSAGE)));


        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,",DE,,")))
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


    @Test
    @DisplayName("If service return empty or null response should return server error")
    void testWeatherHistoryFailureIfServiceReturnsNull() throws Exception {
        doReturn(Optional.ofNullable(null)).when(weatherService).getWeatherHistoryByLocation("Kabul,AF");

        mockMvc.perform(get(MessageFormat.format(ApiConstants.WEATHER_HISTORY_URI,"Kabul,AF")))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_FIELD_MESSAGE,is("System Error")));

    }


    private static Stream<Arguments> weatherDTOListProvider(){
        return Stream.of(
                Arguments.arguments(List.of(new WeatherDTO(1010,90,false),
                        new WeatherDTO(1005,85,false),
                        new WeatherDTO(1020,95,false),
                        new WeatherDTO(1050,105,true),
                        new WeatherDTO(1030,105,false)

                        )

        ));
    }



}
