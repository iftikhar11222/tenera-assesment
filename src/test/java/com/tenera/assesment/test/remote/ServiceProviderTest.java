package com.tenera.assesment.test.remote;
import com.tenera.assesment.dto.GeoCodeInfoDTO;
import com.tenera.assesment.exceptions.InvalidCityNameOrCountryCode;
import com.tenera.assesment.remote.CoordinatesProvider;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ServiceProviderTest {
    @Autowired
    private CoordinatesProvider coordinatesProvider;


    @Test
    @DisplayName("should pass when correct city and country code provided in ISO alpha2")
     void shouldReturnValidResponseIfCorrectCityAndCountryCodeProvided_ISOAlpha2() throws JSONException {
        var geoCodeInfoDTO = GeoCodeInfoDTO.builder().countryCode("DE")
                .cityName("Berlin").build();
        String jsonResult =coordinatesProvider.getGeocodeInfoByLocation(geoCodeInfoDTO);
        Assertions.assertNotNull(jsonResult);
        MockMvcResultMatchers.jsonPath("$[0].lat",is(52.5244));
        MockMvcResultMatchers.jsonPath("$[0].lon",is(13.4105));
        MockMvcResultMatchers.jsonPath("$[0].name",is("Berlin"));
        MockMvcResultMatchers.jsonPath("$[0].country",is("DE"));



     }

    @Test
    @DisplayName("should pass when correct city and country code provided in Iso alpha3")
    void shouldReturnValidResponseIfCorrectCityAndCountryCodeProvided_ISOAlpha3() throws JSONException {
        var geoCodeInfoDTO = GeoCodeInfoDTO.builder().countryCode("DEU")
                .cityName("Berlin").build();
        String jsonResult =coordinatesProvider.getGeocodeInfoByLocation(geoCodeInfoDTO);
        Assertions.assertNotNull(jsonResult);
        MockMvcResultMatchers.jsonPath("$[0].lat",is(52.5244));
        MockMvcResultMatchers.jsonPath("$[0].lon",is(13.4105));
        MockMvcResultMatchers.jsonPath("$[0].name",is("Berlin"));
        MockMvcResultMatchers.jsonPath("$[0].country",is("DE"));



    }

    @Test
    @DisplayName("should pass when correct city name provided")
    void shouldReturnValidResponseIfCorrectCityNameProvide() throws JSONException {
        var geoCodeInfoDTO = GeoCodeInfoDTO.builder()
                .cityName("Berlin").build();
        String jsonResult =coordinatesProvider.getGeocodeInfoByLocation(geoCodeInfoDTO);
        Assertions.assertNotNull(jsonResult);
        MockMvcResultMatchers.jsonPath("$[0].lat",is(52.5244));
        MockMvcResultMatchers.jsonPath("$[0].lon",is(13.4105));
        MockMvcResultMatchers.jsonPath("$[0].name",is("Berlin"));
        MockMvcResultMatchers.jsonPath("$[0].country",is("DE"));
    }
    @Test
    @DisplayName("should throw exception when city name is missing")
    void shouldFailWhenCityIsEmpty(){
        var geoCodeInfoDTO = GeoCodeInfoDTO.builder()
                .cityName("").countryCode("DE").build();

       Throwable exceptionMessage= assertThrows(InvalidCityNameOrCountryCode.class,()->coordinatesProvider.getGeocodeInfoByLocation(geoCodeInfoDTO));
       assertEquals("Wrong city or country name",exceptionMessage.getMessage());


    }


}
