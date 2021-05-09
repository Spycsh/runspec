package com.example.iotinfo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val latitude = MutableLiveData<Double>()

    val longtitude = MutableLiveData<Double>()

    val step = MutableLiveData<Int>()

    val distance = MutableLiveData<Float>()

    val name = MutableLiveData<String>()
}