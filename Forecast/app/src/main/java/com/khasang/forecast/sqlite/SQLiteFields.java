package com.khasang.forecast.sqlite;

/**
 * Created by maxim.kulikov on 02.12.15.
 */

public class SQLiteFields {

    public static final String TABLE_TOWNS = "TOWNS";
    public static final String TABLE_WEATHER = "WEATHER";
    public static final String TABLE_SETTINGS = "SETTINGS";

    public static final String ID = "Id";

    public static final String TOWN = "Town";
    public static final String LATITUDE = "Latitude";
    public static final String LONGTITUDE = "Longtitude";

    public static final String STATION_NAME = "StationName";
    public static final String DATE = "Date";
    public static final String TEMPIRATURE = "Temperature";
    public static final String TEMPIRATURE_MIN = "TemperatureMin";
    public static final String TEMPIRATURE_MAX = "TemperatureMax";
    public static final String PRESSURE = "Pressure";
    public static final String HUMIDITY = "Humidity";
    public static final String DESCRIPTION = "Description";
    public static final String WIND_DIRECTION = "WindDirection";
    public static final String WIND_SPEED = "WindSpeed";
    public static final String PRECIPITATION_TYPE = "PrecipitationType";

    public static final String CURRENT_TEMPIRATURE_METRICS = "CurrentTemperatureMetrics";
    public static final String CURRENT_SPEED_METRICS = "CurrentSpeedMetrics";
    public static final String CURRENT_PRESSURE_METRICS = "CurrentPressureMetrics";
    public static final String CURRENT_STATION = "CurrentServiceType";

    public static final String QUERY_CREATE_TABLE_TOWNS  = "CREATE TABLE " + TABLE_TOWNS + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TOWN + " VARCHAR(30)," +
            LATITUDE + " VARCHAR(30)," +
            LONGTITUDE + " VARCHAR(30))";

    public static final String QUERY_CREATE_TABLE_WEATHER = "CREATE TABLE " + TABLE_WEATHER + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            STATION_NAME + " VARCHAR(30)," +
            TOWN + " VARCHAR(30)," +
            DATE + " DATETIME," +
            TEMPIRATURE + " VARCHAR(10)," +
            TEMPIRATURE_MAX + " VARCHAR(10)," +
            TEMPIRATURE_MIN + " VARCHAR(10)," +
            PRESSURE + " VARCHAR(20)," +
            HUMIDITY + " VARCHAR(20)," +
            DESCRIPTION + " VARCHAR(30)," +
            WIND_DIRECTION + " VARCHAR(20)," +
            WIND_SPEED + " VARCHAR(20)," +
            PRECIPITATION_TYPE + " VARCHAR(20))";

    public static final String QUERY_CREATE_TABLE_SETTINGS = "CREATE TABLE " + TABLE_SETTINGS + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CURRENT_STATION + " VARCHAR(30)," +
            CURRENT_TEMPIRATURE_METRICS + " VARCHAR(30)," +
            CURRENT_SPEED_METRICS + " VARCHAR(30)," +
            CURRENT_PRESSURE_METRICS + " VARCHAR(30))";

    public static final String QUERY_DELETE_TABLE_WEATHER = "DROP TABLE IF EXISTS " + TABLE_WEATHER;
    public static final String QUERY_DELETE_TABLE_TOWNS = "DROP TABLE IF EXISTS " + TABLE_TOWNS;
    public static final String QUERY_DELETE_TABLE_SETTINGS = "DROP TABLE IF EXISTS " + TABLE_SETTINGS;

    public static final String QUERY_DELETE_DATA_WEATHER = "DELETE FROM " + TABLE_WEATHER;
    public static final String QUERY_DELETE_DATA_TOWNS = "DELETE FROM " + TABLE_TOWNS;
    public static final String QUERY_DELETE_DATA_SETTINGS = "DELETE FROM " + TABLE_SETTINGS;

    public static final String QUERY_OBJECTS_COUNT = "SELECT COUNT(*) FROM SQLITE_MASTER WHERE TYPE = ? AND NAME = ?";
    public static final String QUERY_SELECT_TOWNS = "SELECT * FROM " + TABLE_TOWNS;

    public static final String QUERY_SELECT_WEATHER =
            "SELECT * FROM " + TABLE_WEATHER +
            " WHERE " + STATION_NAME + " = ? and " + TOWN + " = ? and " + DATE + " = (SELECT MAX(" + DATE + ") FROM " + TABLE_WEATHER + " WHERE " + DATE + " <= ? )";

    public static final String QUERY_INSERT_TOWN = "INSERT INTO " + TABLE_TOWNS + " (" +
            TOWN + "," +
            LATITUDE + "," +
            LONGTITUDE + ") " +
            " VALUES ( ? , ? , ? )";

    public static final String QUERY_INSERT_WEATHER = "INSERT INTO " + TABLE_WEATHER + " (" +
            STATION_NAME + "," +
            TOWN + "," +
            DATE + "," +
            TEMPIRATURE + "," +
            TEMPIRATURE_MAX + "," +
            TEMPIRATURE_MIN + "," +
            PRESSURE + "," +
            HUMIDITY + "," +
            DESCRIPTION + "," +
            WIND_DIRECTION + "," +
            WIND_SPEED + "," +
            PRECIPITATION_TYPE + ") " +
            " VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";

    public static final String QUERY_INSERT_SETTINGS = "INSERT INTO " + TABLE_SETTINGS + " (" +
            CURRENT_STATION + "," +
            CURRENT_TEMPIRATURE_METRICS + "," +
            CURRENT_SPEED_METRICS + "," +
            CURRENT_PRESSURE_METRICS + ") " +
            " VALUES ( ? , ? , ? , ? )";

    public static final String QUERY_SELECT_SETTINGS =
            "SELECT * FROM " + TABLE_SETTINGS +
            " WHERE ID = (SELECT MAX(" + ID + ") FROM " + TABLE_SETTINGS + ")";
}