package org.runspec.adviser.entity;

public class AdviserInformation {
    private Weather weather;
    private Air air;

    public AdviserInformation(Weather weather, Air air) {
        this.weather = weather;
        this.air = air;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Air getAir() {
        return air;
    }

    public void setAir(Air air) {
        this.air = air;
    }
}
