package com.example.iotinfo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.iotinfo.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(activity!!).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val step: TextView = root.findViewById(R.id.stepData)
        val distance: TextView = root.findViewById(R.id.distanceData)

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

        return root
    }
}