package com.khasang.forecast;

public class Weather {

    private double temperature;
    private double temp_min;
    private double temp_max;
    private double pressure;
    private int humidity;
    private Wind wind;
    private Precipitation precipitation;
    private String description;

    public Weather () {

    }

    public Weather(int temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setWind(Wind.Direction wd, double wp) {
        if (this.wind == null) {
            this.wind = new Wind(wd, wp);
        } else {
            wind.setDirection(wd);
            wind.setSpeed(wp);
        }
    }

    public Wind.Direction getWindDirection() {
        return wind.getDirection();
    }

    public double getWindPower() {
        return wind.getSpeed();
    }

    public void setPrecipitation(Precipitation.Type type, int probability) {
        if (this.precipitation == null) {
            this.precipitation = new Precipitation(type, probability);
        } else {
            this.precipitation.setType(type);
            this.precipitation.setProbability(probability);
        }
    }

    public Precipitation.Type getPrecipitation() {
        return precipitation.getType();
    }

    public int getPrecipitationProbability() {
        return precipitation.getProbability();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}