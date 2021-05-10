package com.example.iotinfo.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.iotinfo.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var root: View
    private var time = 0
    private var running = false
    private lateinit var timeView: TextView
    private lateinit var mHandler: Handler
    private lateinit var togglebutton: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(activity!!).get(DashboardViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val step: TextView = root.findViewById(R.id.stepData)
        val distance: TextView = root.findViewById(R.id.distanceData)
        togglebutton = root.findViewById(R.id.runToggleButton)

        timeView = root.findViewById(R.id.time_view)

        dashboardViewModel.distance.observe(viewLifecycleOwner, Observer {
            try{
                distance.text = "${it.toString().subSequence(0,4)} m"
            } catch (e: Exception) {
                distance.text = "${it.toString()} m"
            }
        })
        dashboardViewModel.step.observe(viewLifecycleOwner, Observer {
            step.text = it.toString()
        })
        dashboardViewModel.time.observe(viewLifecycleOwner, Observer {
            val hours = it / 3600
            val minutes = (it % 3600) / 60
            val seconds = it % 60

            timeView.text = "$hours:$minutes:$seconds"
        })


        togglebutton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                running = true
                dashboardViewModel.time.value = 0
            } else {
                running = false
            }
        }

        runTimer()
        return root
    }

    private fun runTimer(){
        mHandler = Handler(Looper.getMainLooper())
        mStatusChecker.run()
    }

    private fun stopTimer() {
        mHandler.removeCallbacks(mStatusChecker)
    }

    private var mStatusChecker: Runnable = object : Runnable {

        override fun run() {
            try {
                if (running) {
                    dashboardViewModel.time.value = dashboardViewModel.time.value!! + 1
                }
            } finally {
                mHandler.postDelayed(this, 1000)
            }
        }
    }

}