package com.tenera.assesment.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class GeoCodeInfoDTO {

    private String latitude;
    private String longitude;
    private String cityName;
    private String countryCode;
}
