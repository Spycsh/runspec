package com.example.iotinfo.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    val step = MutableLiveData<Int>()

    val distance = MutableLiveData<Float>()

}