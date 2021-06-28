package com.tenera.assesment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class GeoCodeInfoDTO {

    private String latitude;
    private String longitude;
    private String cityName;
    private String countryName;
}
