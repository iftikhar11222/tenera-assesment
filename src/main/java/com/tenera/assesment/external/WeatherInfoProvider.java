package com.tenera.assesment.external;

/**
 *
 * This interface is implemented to call the external weather api
 * @see {@link com.tenera.assesment.external.impl.WeatherInfoProviderImpl}
 * for implementations
 *
 */
public interface WeatherInfoProvider {
    /**
     *
     * @param latitude
     * @param longitude
     * @return raw json returned from the external api
     */
    String getCurrentWeatherInfo(String latitude, String longitude);

    String getHistoricalWeatherInfo(String latitude, String longitude);
}
