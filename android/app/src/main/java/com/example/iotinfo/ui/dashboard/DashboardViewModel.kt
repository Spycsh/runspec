package com.example.iotinfo.ui.dashboard

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.example.iotinfo.CustomJsonRequest
import org.json.JSONArray
import org.json.JSONObject

class DashboardViewModel : ViewModel() {

    val step = MutableLiveData<Int>()

    val distance = MutableLiveData<Float>()

    val time = MutableLiveData(0)

    val tripLock = MutableLiveData(false)

    val myTrip = MutableLiveData<JSONArray>()

    val mHandler: Handler = Handler(Looper.getMainLooper())

    val tripId = MutableLiveData(System.currentTimeMillis().toString())

    var mStatusChecker: Runnable = object : Runnable {

        override fun run() {
            try {
                if (tripLock.value!!) {
                    time.value = time.value!! + 1
                }
            } finally {
                mHandler.postDelayed(this, 1000)
            }
        }
    }

    fun newTripId(){
        tripId.value = System.currentTimeMillis().toString()
    }

    fun updateMyTrip(hostname: String, username: String, queue: RequestQueue){

        val url = "http://" + hostname + ":8182/producer/returnTripData"

        val request = JSONObject()
        request.put("userId", username)
        request.put("tripId", tripId.value)

        val jsonRequest = CustomJsonRequest(
            Request.Method.POST, url, request,

            { response ->
                Log.d("API", response.toString())
                myTrip.value = response
            },
            { error ->
                Log.d("API", "error => $error")
            })

        queue.add(jsonRequest)
    }

}