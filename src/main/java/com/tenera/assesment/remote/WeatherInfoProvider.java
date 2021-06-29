package com.tenera.assesment.remote;

public interface WeatherInfoProvider {

    String getCurrentWeatherInfo(String latitude, String longitude);

    String getHistoricalWeatherInfo(String latitude, String longitude);
}
