package com.tenera.assesment.mapper;

import com.tenera.assesment.dto.GeoCodeInfoDTO;

import java.util.Optional;

public interface GeoCodingResponseMapper {

    Optional<GeoCodeInfoDTO> mapJsonToGeoCodingDTO(String json);
}
