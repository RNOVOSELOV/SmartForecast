package com.khasang.forecast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Роман on 26.11.2015.
 */
public class WeatherStationFactory {
    public enum ServiceType {OPEN_WEATHER_MAP}

    private HashMap<ServiceType, WeatherStation> stations;

    public WeatherStationFactory() {
        stations = new HashMap<ServiceType, WeatherStation>();
    }

    public WeatherStationFactory addOpenWeatherMap() {
        WeatherStation ws = new OpenWeatherMap();
        String name = "OpenWeatherMap";
        ws.serviceType = ServiceType.OPEN_WEATHER_MAP;
        stations.put(ServiceType.OPEN_WEATHER_MAP, ws);
        return this;
    }

    /*

    При добадении новых сервисов добавить в билдер строитель для каждого сервиса по типу
    public Builder addOpenWeatherMap

    */

    public HashMap<ServiceType, WeatherStation> create() {
        return stations;
    }

}
