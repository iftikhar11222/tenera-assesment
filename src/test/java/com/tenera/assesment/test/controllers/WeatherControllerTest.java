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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {


   @Autowired private MockMvc mockMvc;
   @MockBean private WeatherService weatherService;

    @Test
    @DisplayName("should return success if valid location is provided in query params")
    public void testCurrentWeatherSuccessIfValidLocation() throws Exception {

        WeatherDTO weatherDTO = new WeatherDTO(100,1000,false);
        doReturn(weatherDTO).when(weatherService).getCurrentWeatherByCity("Berlin,DE");
       mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"Berlin,DE")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pressure",is(instanceOf(Integer.class))))
                .andExpect( jsonPath("$.temperature",is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.umbrella",is(instanceOf(Boolean.class))));
    }


    @Test
    @DisplayName("should fail and throw constraint violation exception if invalid location format")
    public void testCurrentWeatherFailureIfInvalidLocationFormat() throws Exception {
        mockMvc.perform(get(MessageFormat.format(ApiConstants.CURRENT_WEATHER_URI,"Ber123,DE,,")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message",is("Invalid Request")));

    }



}