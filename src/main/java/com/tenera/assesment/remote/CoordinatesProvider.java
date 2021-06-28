package com.tenera.assesment.remote;

import com.tenera.assesment.dto.GeoCodeInfoDTO;

import java.util.Optional;

public interface CoordinatesProvider {

     Optional<GeoCodeInfoDTO> getGeocodeInfoByLocation(String location);


}
