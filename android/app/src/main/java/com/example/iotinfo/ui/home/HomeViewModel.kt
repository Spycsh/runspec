package com.example.iotinfo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

class HomeViewModel : ViewModel() {

    val latitude = MutableLiveData<Double>()

    val longtitude = MutableLiveData<Double>()

    val name = MutableLiveData<String>()

    val advise = MutableLiveData<JSONObject>()
}