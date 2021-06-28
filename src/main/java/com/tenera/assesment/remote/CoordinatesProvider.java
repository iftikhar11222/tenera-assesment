package com.tenera.assesment.remote;

import com.tenera.assesment.dto.GeoCodeInfoDTO;


public interface CoordinatesProvider {
     String getGeocodeInfoByLocation(GeoCodeInfoDTO location);

}
