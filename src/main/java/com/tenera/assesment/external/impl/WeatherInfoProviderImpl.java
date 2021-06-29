package com.tenera.assesment.external.impl;

import com.tenera.assesment.exceptions.ExternalApiException;
import com.tenera.assesment.external.WeatherInfoProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeatherInfoProviderImpl implements WeatherInfoProvider {

    private RestTemplate restTemplate;

    @Value("#{'${open.weather.api.exlude.options}'.split(',')}")
    private List<String> excludeOptions;
    @Value("${api.id}")
    String apiId;
    @Value("${weather.api.base.url}")
    String weatherApiBaseUrl;

    /**
     *
     * @param latitude
     * @param longitude
     * @return Raw json string returned from external api for current weather info
     */
    @Override
    public String getCurrentWeatherInfo(String latitude, String longitude) {
        log.info("{longitude :" + longitude + ",latitude :" + latitude);
        try {
            var url = weatherApiUrlBuilder(latitude, longitude, "current");
            var rawResponse = restTemplate.getForEntity(url, String.class);
            log.info("response status:" + rawResponse.getStatusCode().value());
            return rawResponse.getBody();
        } catch (Exception ex) {
            log.error("", ex);
            throw new ExternalApiException(ex.getMessage());
        }
    }

    /**
     *
     * @param latitude
     * @param longitude
     * @return return the raw json response returned from external api for weather history
     */
    @Override
    public String getHistoricalWeatherInfo(String latitude, String longitude) {
        log.info("{longitude :" + longitude + ",latitude :" + latitude);
        try {
            var url = weatherApiUrlBuilder(latitude, longitude, "daily");
            var rawResponse = restTemplate.getForEntity(url, String.class);
            log.info("response status:" + rawResponse.getStatusCode().value());
            return rawResponse.getBody();
        } catch (Exception ex) {
            log.error("", ex);
            throw new ExternalApiException("Response Code= " + ex.getMessage());
        }

    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * build the uri.configure the request params and return the url in string format
     * @param latitude
     * @param longitude
     * @param excludeOption
     * @return  Url in string format
     */
    private String weatherApiUrlBuilder(String latitude, String longitude, String excludeOption) {
        return UriComponentsBuilder.newInstance().scheme("https").host(weatherApiBaseUrl)
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("exclude", excludeOptions.stream().filter(item -> !item.equals(excludeOption)).collect(Collectors.joining(",")))
                .queryParam("units", "metric")
                .queryParam("lang", "en")
                .queryParam("appid", apiId).build().toUriString();
    }
}

