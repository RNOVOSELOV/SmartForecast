package com.khasang.forecast;

/**
 * Created by novoselov on 24.11.2015.
 */

public abstract class WeatherStation {
    String weatherStationName;

    WeatherStationFactory.WEATHER_SERVICE_TYPE serviceType;

    public WeatherStationFactory.WEATHER_SERVICE_TYPE getServiceType() {
        return serviceType;
    }

    public String getWeatherStationName() {
        return weatherStationName;
    }

    abstract void updateWeather(ILocation loc);

    abstract void updateHourlyWeather(ILocation loc);

    abstract void updateWeeklyWeather(ILocation loc);
}
