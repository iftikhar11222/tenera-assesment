package com.tenera.assesment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
public class WeatherHistoryDTO {

    public WeatherHistoryDTO(List<WeatherDTO> history){
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

    private void calculateAveragePressure() {
        if(history!=null && history.size()>0){
            this.averagePressure=  (int) this.history.stream()
                    .mapToInt(node->node.getPressure())
                    .average().getAsDouble();
        }
    }

    private void calculateAverageTemperature() {
        if(history!=null && history.size()>0){
            this.averageTemperature=(int) this.history.stream()
                    .mapToInt(node->node.getTemperature())
                    .average().getAsDouble();
        }

    }


}
