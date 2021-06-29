package com.tenera.assesment.external;

import com.tenera.assesment.dto.GeoCodeInfoDTO;

/**
 * This api accepts the city nam and optional 2-3 digit country code
 * and returns the geo graphical coordinates
 *
 */
public interface LocationProvider {
    /**
     *
     * @param location
     * @return raw json response in string format
     */
    String getGeocodeInfoByLocation(GeoCodeInfoDTO location);

}
