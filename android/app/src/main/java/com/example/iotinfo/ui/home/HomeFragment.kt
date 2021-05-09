package com.example.iotinfo.ui.home

import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.iotinfo.R
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(activity!!).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val latitude: TextView = root.findViewById(R.id.latitudeData)
        val longtitude: TextView = root.findViewById(R.id.longitudeData)
        val step: TextView = root.findViewById(R.id.stepData)
        val distance: TextView = root.findViewById(R.id.distanceData)
        val name: TextView = root.findViewById(R.id.helloText)

        homeViewModel.latitude.observe(viewLifecycleOwner, Observer {
            latitude.text = it.toString()
        })
        homeViewModel.longtitude.observe(viewLifecycleOwner, Observer {
            longtitude.text = it.toString()
        })
        homeViewModel.distance.observe(viewLifecycleOwner, Observer {
            distance.text = "$it m"
        })
        homeViewModel.step.observe(viewLifecycleOwner, Observer {
            step.text = it.toString()
        })
        homeViewModel.name.observe(viewLifecycleOwner, Observer {
            name.text = it.toString()
        })

        return root
    }

}