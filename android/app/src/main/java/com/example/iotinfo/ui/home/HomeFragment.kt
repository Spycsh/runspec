package com.example.iotinfo.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iotinfo.R
import com.example.iotinfo.ui.PopAdapter
import org.json.JSONObject


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(activity!!).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val latitude: TextView = root.findViewById(R.id.latitudeData)
        val longitude: TextView = root.findViewById(R.id.longitudeData)
        val name: TextView = root.findViewById(R.id.helloText)
        val currentWeather: TextView = root.findViewById(R.id.weather_currentWeather)
        val humidity: TextView = root.findViewById(R.id.weather_humidity)
        val temperature: TextView = root.findViewById(R.id.weather_temperature)
        val nextHour0Weather: TextView = root.findViewById(R.id.weather_nextHour0Weather)
        val nextHour1Weather: TextView = root.findViewById(R.id.weather_nextHour1Weather)
        val nextHour2Weather: TextView = root.findViewById(R.id.weather_nextHour2Weather)
        val aqi: TextView = root.findViewById(R.id.air_aqi)
        val pm2_5: TextView = root.findViewById(R.id.air_pm2_5)
        val co: TextView = root.findViewById(R.id.air_co)
        val no2: TextView = root.findViewById(R.id.air_no2)
        val popList: RecyclerView = root.findViewById(R.id.pop_list)

        popList.layoutManager = LinearLayoutManager(activity)

        homeViewModel.latitude.observe(viewLifecycleOwner, Observer {
            latitude.text = it.toString()
        })
        homeViewModel.longtitude.observe(viewLifecycleOwner, Observer {
            longitude.text = it.toString()
        })
        homeViewModel.name.observe(viewLifecycleOwner, Observer {
            name.text = it.toString()
        })
        homeViewModel.advise.observe(viewLifecycleOwner, Observer {
            currentWeather.text = "Weather: ${(it.get("weather") as JSONObject).get("currentWeather")}"
            humidity.text = "Humidity: ${(it.get("weather") as JSONObject).get("humidity")}%"
            temperature.text = "Weather: ${(it.get("weather") as JSONObject).get("temperature").toString().subSequence(0,3)}℃"
            nextHour0Weather.text = "1 Hour Later: ${(it.get("weather") as JSONObject).get("nextHour0Weather")}"
            nextHour1Weather.text = "2 Hour Later: ${(it.get("weather") as JSONObject).get("nextHour1Weather")}"
            nextHour2Weather.text = "3 Hour Later: ${(it.get("weather") as JSONObject).get("nextHour2Weather")}"
            aqi.text = "AQI: ${(it.get("air") as JSONObject).get("aqi")}"
            pm2_5.text = "PM2.5: ${(it.get("air") as JSONObject).get("pm2_5")}"
            no2.text = "NO2: ${(it.get("air") as JSONObject).get("no2")}"
            co.text = "CO: ${(it.get("air") as JSONObject).get("co")}"
        })
        homeViewModel.popLocation.observe(viewLifecycleOwner, Observer {
            popList.adapter = PopAdapter(it)
        })

        return root
    }

}