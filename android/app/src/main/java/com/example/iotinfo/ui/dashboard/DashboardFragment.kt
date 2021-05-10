package com.example.iotinfo.ui.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.example.iotinfo.MainActivity
import com.example.iotinfo.R
import com.example.iotinfo.ui.TripAdapter
import com.example.iotinfo.ui.home.HomeViewModel
import org.json.JSONArray

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var root: View
    private lateinit var timeView: TextView
    private lateinit var tripText: TextView
    private lateinit var togglebutton: ToggleButton
    private lateinit var sharedPref: SharedPreferences
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var trip: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(activity!!).get(DashboardViewModel::class.java)
        homeViewModel =
            ViewModelProvider(activity!!).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        sharedPref = activity!!.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val step: TextView = root.findViewById(R.id.stepData)
        val distance: TextView = root.findViewById(R.id.distanceData)

        trip = root.findViewById(R.id.my_list)
        tripText = root.findViewById(R.id.trip_word)
        trip.layoutManager = LinearLayoutManager(activity)

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

        togglebutton.isChecked = dashboardViewModel.tripLock.value!!

        togglebutton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                runTimer()
            } else {
                stopTimer()
                dashboardViewModel.updateMyTrip(
                    sharedPref.getString(getString(R.string.saved_url),"")!!,
                    sharedPref.getString(getString(R.string.saved_user_name),"")!!,
                    (activity as MainActivity).getRequestQueue()
                    )
            }
        }
        dashboardViewModel.myTrip.observe(viewLifecycleOwner, Observer {
            trip.adapter = homeViewModel.popLocation.value?.let { it1 -> TripAdapter(it, it1) }
            if(it.length()==0){
                showTrip(true)
            } else {
                showTrip(false)
            }
        })
        return root
    }

    private fun runTimer(){
        dashboardViewModel.tripLock.value = true
        dashboardViewModel.time.value = 0
        dashboardViewModel.mStatusChecker.run()
        dashboardViewModel.newTripId()
    }

    private fun stopTimer() {
        dashboardViewModel.tripLock.value = false
        dashboardViewModel.mHandler.removeCallbacks(dashboardViewModel.mStatusChecker)
    }

    private fun showTrip(lock: Boolean) {
        if (lock){
            tripText.visibility = View.INVISIBLE
            trip.visibility = View.INVISIBLE
        } else {
            tripText.visibility = View.VISIBLE
            trip.visibility = View.VISIBLE
        }
    }

}