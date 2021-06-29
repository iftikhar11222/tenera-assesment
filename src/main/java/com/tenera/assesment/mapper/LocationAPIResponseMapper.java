package com.tenera.assesment.mapper;

import com.tenera.assesment.dto.GeoCodeInfoDTO;

import java.util.Optional;

/**
 * This is used to map the json response returned by Open Weather Geocoding
 * info
 *
 */
public interface LocationAPIResponseMapper {

    Optional<GeoCodeInfoDTO> mapJsonToGeoCodingDTO(String json);
}
