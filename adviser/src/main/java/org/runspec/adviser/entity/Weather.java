package org.runspec.adviser.entity;


import java.util.ArrayList;
import java.util.List;

public class Weather {
    private String currentWeather;
    private boolean badCurrentWeather;
    private List<String> weatherWithInTwoHours;
    private String temperature;
    private String visibility;
    private String humidity;
    private String windSpeed;
    private String uvi;

    public Weather(){
        weatherWithInTwoHours = new ArrayList<String>();
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

    public void addFutureWeather(String futureWeather) {
        this.weatherWithInTwoHours.add(futureWeather) ;
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
