package com.tenera.assesment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public class WeatherHistoryDTO {

    public WeatherHistoryDTO(List<WeatherDTO> history) {
        this.history = history;
        calculateAverageTemperature();
        calculateAveragePressure();
    }


    @Getter
    @Setter
    @JsonProperty("average_temperature")
    int averageTemperature;
    @Getter
    @Setter
    @JsonProperty("average_pressure")
    int averagePressure;
    @Getter
    @Setter
    List<WeatherDTO> history;

    /**
     * method calculate the average pressure and save it to field
     *
     */
    private void calculateAveragePressure() {
        if (history != null && !history.isEmpty()) {
            this.averagePressure = (int) this.history.stream()
                    .mapToInt(WeatherDTO::getPressure)
                    .average().getAsDouble();
        }
    }
    /**
     * method calculate the temperature pressure and save it to field
     *
     */
    private void calculateAverageTemperature() {
        if (history != null && !history.isEmpty()) {
            this.averageTemperature = (int) this.history.stream()
                    .mapToInt(WeatherDTO::getTemperature)
                    .average().getAsDouble();
        }

    }


}
