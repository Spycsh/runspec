package com.example.iotinfo.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import org.json.JSONObject

class HomeViewModel : ViewModel() {

    val latitude = MutableLiveData<Double>()

    val longtitude = MutableLiveData<Double>()

    val name = MutableLiveData<String>()

    val advise = MutableLiveData<JSONObject>()

    val popLocation = MutableLiveData<JSONArray>()

    fun updatePopLocation(hostname: String, queue: RequestQueue){
        val url = "http://" + hostname + ":8182/producer/hotSpot"

        val jsonArray: JSONArray = JSONArray("[{\"name\": \"Place\",\"count\": \"Popularity\"}]")

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.POST, url,null,
            { response ->
                Log.d("API", response.toString())

                for (i in 0 until response.length())
                    jsonArray.put(response.get(i))
                popLocation.value = jsonArray
            },
            { error ->
                Log.d("API", "error => $error")
            })

        queue.add(jsonArrayRequest)
    }
}