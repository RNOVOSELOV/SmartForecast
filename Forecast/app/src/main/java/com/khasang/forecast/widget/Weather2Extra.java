package com.khasang.forecast.widget;

import android.content.Intent;
import android.os.Bundle;

import com.khasang.forecast.Precipitation;
import com.khasang.forecast.Weather;
import com.khasang.forecast.Wind;

/**
 * Created by baradist on 27.12.2015.
 * Временный класс для передачи Weather через Intent
 */
public class Weather2Extra {
    public static final String KEY_WEATHER_DESCRIPTION = "key_weather_description";
    public static final String KEY_WEATHER_WIND_DIRECTION = "key_weather_wind_direction";
    public static final String KEY_WEATHER_WIND_POWER = "key_weather_wind_power";
    public static final String KEY_WEATHER_HUMIDITY= "key_weather_humidity";
    public static final String KEY_WEATHER_TEMPERATURE = "key_weather_temperature";
    public static final String KEY_WEATHER_PRESSURE = "key_weather_pressure";
    public static final String KEY_WEATHER_PRECIPITATION = "key_weather_precipitation";
    public static final String KEY_DATE = "key_date";
    public final static String KEY_CURRENT_POSITION = "key_current_position_";
    public final static String KEY_CURRENT_POSITION_ID = "key_current_position_id";


    public static Intent putWeatherToExtra(Intent intent, Weather weather) {
        Bundle extra = intent.getBundleExtra("name");
        // TODO: Bundle extra
        intent.putExtra(KEY_WEATHER_TEMPERATURE, weather.getTemperature());
        intent.putExtra(KEY_WEATHER_PRESSURE, weather.getPressure());
        intent.putExtra(KEY_WEATHER_PRECIPITATION, weather.getPrecipitation().toString());
        intent.putExtra(KEY_WEATHER_DESCRIPTION, weather.getDescription());
        intent.putExtra(KEY_WEATHER_WIND_DIRECTION, weather.getWindDirection().getDirectionString());
        intent.putExtra(KEY_WEATHER_WIND_POWER, weather.getWindPower());
        intent.putExtra(KEY_WEATHER_HUMIDITY, weather.getHumidity());

        return intent;
    }

    public static Weather getWeatherFromExtra(Bundle extras) {
//        double temperature, double temp_min, double temp_max,
//        double pressure, int humidity, Wind wind, Precipitation precipitation,
//                String description
        Weather weather = null;
        try {
//            double temperature = intent.getDoubleExtra(KEY_WEATHER_TEMPERATURE, 0d);
//            double pressure = intent.getDoubleExtra(KEY_WEATHER_PRESSURE, 0d);
//            int humidity = intent.getIntExtra(KEY_WEATHER_HUMIDITY, 0);
//            String direction1 = intent.getStringExtra(KEY_WEATHER_WIND_DIRECTION);
//            Wind.Direction direction = Wind.stringToDirection(direction1);
//            double speed = intent.getDoubleExtra(KEY_WEATHER_WIND_POWER, 0d);
//            Wind wind = new Wind(direction, speed);
//            String type = intent.getStringExtra(KEY_WEATHER_PRECIPITATION);
//            Precipitation.Type type1 = Precipitation.stringToType(type);
//            Precipitation precipitation = null;
//            if (type1 != null) {
//                precipitation = new Precipitation(type1);
//            }
//            String description = intent.getStringExtra(KEY_WEATHER_DESCRIPTION);
            double temperature = extras.getDouble(KEY_WEATHER_TEMPERATURE, 0d);
            double pressure = extras.getDouble(KEY_WEATHER_PRESSURE, 0d);
            int humidity = extras.getInt(KEY_WEATHER_HUMIDITY, 0);
            String direction1 = extras.getString(KEY_WEATHER_WIND_DIRECTION);
            Wind.Direction direction = Wind.stringToDirectionStatic(direction1); // TODO: Wind.stringToDirection
            double speed = extras.getDouble(KEY_WEATHER_WIND_POWER, 0d);
            Wind wind = new Wind(direction, speed);
            String type = extras.getString(KEY_WEATHER_PRECIPITATION);
            Precipitation.Type type1 = Precipitation.stringToTypeStatic(type); // TODO: Precipitation.stringToTypeStatic
            Precipitation precipitation = null;
            if (type1 != null) {
                precipitation = new Precipitation(type1);
            }
            String description = extras.getString(KEY_WEATHER_DESCRIPTION);

            weather = new Weather(temperature
                    , 0d, 0d
                    , pressure
                    , humidity
                    , wind
                    , precipitation
                    , description
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;
    }
}
