package com.tenera.assesment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
/**
 * POJO to hold the weather info
 */
public class WeatherDTO {

    private int pressure;
    private int temperature;
    private boolean umbrella;


}
