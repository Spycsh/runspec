package com.example.iotinfo.ui.dashboard

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    val step = MutableLiveData<Int>()

    val distance = MutableLiveData<Float>()

    val time = MutableLiveData<Int>(0)

    val tripLock = MutableLiveData<Boolean>(false)

    val mHandler: Handler = Handler(Looper.getMainLooper())

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

}