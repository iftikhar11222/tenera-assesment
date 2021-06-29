package com.tenera.assesment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WeatherDTO {

    private int pressure;
    private int temperature;
    private boolean umbrella;



}
