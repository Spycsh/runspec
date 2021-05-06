package org.runspec.adviser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import org.apache.tomcat.jni.Address;
import org.apache.tomcat.util.json.JSONParser;
import org.runspec.adviser.entity.AdviserInformation;
import org.runspec.adviser.entity.Air;
import org.runspec.adviser.entity.Weather;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



@RestController
public class AdviserService {

    String open_weather_api_key = "59448db54000f612be094c8dbeab93cb";



    @PostMapping(value = "/adviser/info")
    @ResponseBody
    public String getWeather(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) throws IOException, InterruptedException {
        String presentInfo = "";


        Weather weatherResult = checkWeather(longitude, latitude);
        Air airResult = checkAir(longitude, latitude);

        AdviserInformation ai = new AdviserInformation(weatherResult, airResult);

        presentInfo = JSONObject.toJSONString(ai);

        return presentInfo;
    }

    public Weather checkWeather(String longitude, String latitude) throws IOException, InterruptedException {
        Weather weather = new Weather();

        //get weather from open weather map api: current weather and hourly forecast weather
        String weather_query = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude + "&lon=" + longitude + "&exclude=minutely,daily&appid=" + open_weather_api_key;
        HttpRequest weather_request = HttpRequest.newBuilder()
                .uri(URI.create(weather_query))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> weather_response = HttpClient.newHttpClient().send(weather_request, HttpResponse.BodyHandlers.ofString());

        JSONObject result = JSON.parseObject(weather_response.body());
        JSONObject current = result.getJSONObject("current");


        String humidity = current.getString("humidity");
        weather.setHumidity(humidity);

        String temp_kel = current.getString("temp");
        double temp = Double.parseDouble(temp_kel) - 273.15;
        weather.setTemperature(Double.toString(temp));


        String visibility = current.getString("visibility");
        weather.setVisibility(visibility);

        String windSpeed = current.getString("wind_speed");
        weather.setWindSpeed(windSpeed);

        String uvi = current.getString("uvi");
        weather.setUvi(uvi);
        weather.checkUvi();

        String main = current.getJSONArray("weather").getJSONObject(0).getString("main");
        weather.setCurrentWeather(main);

        String weather_id_current = current.getJSONArray("weather").getJSONObject(0).getString("id");
        weather.setBadCurrentWeather(!checkWeatherId(weather_id_current));

        String weather_0_hour = result.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");
        String weather_1_hour = result.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");
        String weather_2_hour = result.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");

        String weather_0_hour_id = result.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("id");
        String weather_1_hour_id = result.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("id");
        String weather_2_hour_id = result.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("id");


        weather.setNextHour0Weather(weather_0_hour);
        weather.setNextHour1Weather(weather_1_hour);
        weather.setNextHour2Weather(weather_2_hour);
        weather.setBadFutureWeather((!checkWeatherId(weather_0_hour_id))||(!checkWeatherId(weather_1_hour_id))||(!checkWeatherId(weather_2_hour_id)));

        return weather;
    }

    //if weather is bad, return false
    public boolean checkWeatherId(String id) {
        char begin = id.charAt(0);
        //https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
        if (begin == '2' || begin == '3' || begin == '5' || begin == '6' || begin == '7') {
            return false;
        }
        return true;
    }



    public Air checkAir(String longitude, String latitude) throws IOException, InterruptedException {
        //get air condition from open weather map api

        Air air = new Air();

        String air_query = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=" + open_weather_api_key;
        HttpRequest air_request = HttpRequest.newBuilder()
                .uri(URI.create(air_query))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> air_response = HttpClient.newHttpClient().send(air_request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(air_response.body());

        JSONObject result = JSON.parseObject(air_response.body());
        JSONObject current_air = result.getJSONArray("list").getJSONObject(0);

        //1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor
        String aqi = current_air.getJSONObject("main").getString("aqi");
        air.setAqi(checkAqi(Integer.parseInt(aqi)));

        //WHO standard - 2005
        String co = current_air.getJSONObject("components").getString("co");
        air.setCo(co);
        String no = current_air.getJSONObject("components").getString("no");
        air.setNo(no);
        String no2 = current_air.getJSONObject("components").getString("no2");
        air.setNo2(no2);
        String o3 = current_air.getJSONObject("components").getString("o3");
        air.setO3(o3);
        String so2 = current_air.getJSONObject("components").getString("so2");
        air.setSo2(so2);
        String pm2_5 = current_air.getJSONObject("components").getString("pm2_5");
        air.setPm2_5(pm2_5);
        String pm10 = current_air.getJSONObject("components").getString("pm10");
        air.setPm10(pm10);
        String nh3 = current_air.getJSONObject("components").getString("nh3");
        air.setNh3(nh3);
        return air;
    }

    //check aqi status
    public String checkAqi(int aqi){
        switch(aqi){
            case 1 :
                return "good";
            case 2 :
                return "fair";
            case 3 :
                return "moderate";
            case 4 :
                return "poor";
            case 5 :
                return "very poor";
            default :
                return "unknown";
        }

    }
}
