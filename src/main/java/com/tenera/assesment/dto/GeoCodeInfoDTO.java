package com.tenera.assesment.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
/**
 * This pojo is used to hold the coordinate info as open weather api required the
 * coordinate latitude and longitude to return the weather info
 */
public class GeoCodeInfoDTO {

    private String latitude;
    private String longitude;
    private String cityName;
    private String countryCode;
}
