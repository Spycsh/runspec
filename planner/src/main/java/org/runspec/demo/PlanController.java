package org.runspec.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.geocoder.Geocoder;

import org.apache.tomcat.jni.Address;


import org.apache.tomcat.util.json.JSONParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Locale;

@RestController
public class PlanController {


    String open_weather_api_key = "59448db54000f612be094c8dbeab93cb";
    boolean recommend = true;

    @PostMapping(value= "/plan")
    @ResponseBody
    public String getWeather(@RequestParam("latitude") String latitude,
                             @RequestParam("longtitude") String longtitude) throws IOException, InterruptedException {

//        String geocoder_query = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longtitude +"&key=" + google_api_key;
//        HttpRequest request_city = HttpRequest.newBuilder()
//
//                .uri(URI.create(geocoder_query))
//                .method("GET", HttpRequest.BodyPublishers.noBody())
//                .build();
//        HttpResponse<String> response_city = HttpClient.newHttpClient().send(request_city, HttpResponse.BodyHandlers.ofString());
//
//        System.out.println(response_city.body());
//
//
//
//        JSONObject jsonObject = JSON.parseObject(response_city.body());
//        JSONArray results = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
//        for(int i = 0; i < results.size(); i++){
//            JSONObject temp = results.getJSONObject(i);
//            JSONArray type = temp.getJSONArray("types");
//            if(type.size() == 2 && (type.get(0).equals("administrative_area_level_1") || type.get(1).equals("administrative_area_level_1") )){
//                city = temp.getString("long_name");
//            }
//
//        }
//        System.out.println("***********************");
//        System.out.println("city from google geocoder api: " + city);
//        System.out.println("***********************");
//
//
//
//        String  open_weather_query = "https://community-open-weather-map.p.rapidapi.com/find?q=" + city.trim() + "&cnt=1&mode=JSON&lon=" + longtitude + "&type=link%2C%20accurate&lat="+ latitude + "&units=imperial%2C%20metric";
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(open_weather_query))
//                .header("x-rapidapi-key", open_weather_api_key)
//                .header("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
//                .method("GET", HttpRequest.BodyPublishers.noBody())
//                .build();
//        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//
//        System.out.println(response.body());
//
        String weatherResult = checkWeather(longtitude, latitude);

       String airResult = checkAir(longtitude, latitude);




        return "success";
    }

    public String checkWeather(String longtitude, String latitude) throws IOException, InterruptedException {

        //get weather from open weather map api: current weather and hourly forecast weather
        String weather_query = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude + "&lon=" + longtitude + "&exclude=minutely,daily&appid=" + open_weather_api_key;
        HttpRequest weather_request = HttpRequest.newBuilder()
                .uri(URI.create(weather_query))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> weather_response = HttpClient.newHttpClient().send(weather_request, HttpResponse.BodyHandlers.ofString());

        //System.out.println(weather_response.body());

        JSONObject result = JSON.parseObject(weather_response.body());
        JSONObject current = result.getJSONObject("current");
        String humidity = current.getString("humidity");
        String temp = current.getString("temp");
        String visibility = current.getString("visibility");
        String windSpeed = current.getString("wind_speed");
        String uvi = current.getString("uvi");

        String main = current.getJSONArray("weather").getJSONObject(0).getString("main");
        String weather_id_current = current.getJSONArray("weather").getJSONObject(0).getString("id");
        String weather_id_0_hour = result.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("id");
        String weather_id_1_hour = result.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("id");
        String weather_id_2_hour = result.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("id");



        return "";
    }


    public boolean checkWeatherId(String id){
        char begin = id.charAt(0);
        //https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
        if(begin == '2' || begin == '3' || begin == '5' || begin == '6' || begin == '7'){
            return false;
        }
        return true;
    }


    public String checkAir(String longtitude, String latitude) throws IOException, InterruptedException {
        //get air condition from open weather map api
        String air_query = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat=" + latitude + "&lon=" + longtitude + "&appid=" + open_weather_api_key;
        HttpRequest air_request = HttpRequest.newBuilder()
                .uri(URI.create(air_query))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> air_response = HttpClient.newHttpClient().send(air_request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(air_response.body());

        JSONObject result = JSON.parseObject(air_response.body());
        JSONObject current_air = result.getJSONArray("list").getJSONObject(0);
        String aqi = current_air.getJSONObject("main").getString("aqi");
        String co = current_air.getJSONObject("components").getString("co");
        String no = current_air.getJSONObject("components").getString("no");
        String no2 = current_air.getJSONObject("components").getString("no2");
        String o3 = current_air.getJSONObject("components").getString("o3");
        String so2 = current_air.getJSONObject("components").getString("so2");
        String pm2_5 = current_air.getJSONObject("components").getString("pm2_5");
        String pm10 = current_air.getJSONObject("components").getString("pm10");
        String nh3 = current_air.getJSONObject("components").getString("nh3");

        System.out.println("aqi" + aqi);
        System.out.println("pm2_5" + pm2_5);

        return "";
    }
}
