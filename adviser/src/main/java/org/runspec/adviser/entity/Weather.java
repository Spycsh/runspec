package org.runspec.adviser.entity;


import java.util.ArrayList;
import java.util.List;

public class Weather {
    private String currentWeather;
    private boolean badCurrentWeather;
    private String nextHour0Weather;
    private String nextHour1Weather;
    private String nextHour2Weather;
    private boolean badFutureWeather;
    private String temperature;
    private String visibility;
    private String humidity;
    private String windSpeed;
    private String uvi;

    public Weather(){

    }


    public void checkUvi() {
        //https://www.epa.gov/sunsafety/uv-index-scale-0
        //0-2, 3-7, 8+
        Double num = Double.parseDouble(uvi);
        if(num <= 2.0 && num >= 0.0){
            uvi = uvi + ", low";
        }else if(num <= 7.0 && num >= 3.0){
            uvi = uvi + ", medium";
        } else if(num >= 8.0){
            uvi = uvi + ", high";
        }
    }

    public boolean isBadFutureWeather() {
        return badFutureWeather;
    }

    public void setBadFutureWeather(boolean badFutureWeather) {
        this.badFutureWeather = badFutureWeather;
    }

    public String getNextHour0Weather() {
        return nextHour0Weather;
    }

    public void setNextHour0Weather(String nextHour0Weather) {
        this.nextHour0Weather = nextHour0Weather;
    }

    public String getNextHour1Weather() {
        return nextHour1Weather;
    }

    public void setNextHour1Weather(String nextHour1Weather) {
        this.nextHour1Weather = nextHour1Weather;
    }

    public String getNextHour2Weather() {
        return nextHour2Weather;
    }

    public void setNextHour2Weather(String nextHour2Weather) {
        this.nextHour2Weather = nextHour2Weather;
    }

    public boolean isBadCurrentWeather() {
        return badCurrentWeather;
    }

    public void setBadCurrentWeather(boolean badCurrentWeather) {
        this.badCurrentWeather = badCurrentWeather;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;
    }


    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getUvi() {
        return uvi;
    }

    public void setUvi(String uvi) {
        this.uvi = uvi;
    }
}
