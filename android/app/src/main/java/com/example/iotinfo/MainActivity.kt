package com.example.iotinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.iotinfo.ui.dashboard.DashboardViewModel
import com.example.iotinfo.ui.home.HomeViewModel
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.nio.charset.Charset


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private var requestingLocationUpdates = true
    private lateinit var locationCallback: LocationCallback
    private var mLocation: Location? = null

    private lateinit var latitudeData: TextView
    private lateinit var longitudeData: TextView
    private lateinit var stepData: TextView
    private lateinit var distanceData: TextView

    private lateinit var nameText: EditText
    private lateinit var urlText: EditText
    private lateinit var helloText: TextView
//
//    private lateinit var connectButton: Button

    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor

    private var initSteps = 0f

    private var distance = 0f
    private lateinit var sharedPref: SharedPreferences

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var queue : RequestQueue

    private var tripId = System.currentTimeMillis().toString()

    private var tripLock = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        dashboardViewModel.tripLock.observe(this, Observer {
            tripLock = it
        })

        // Get text views
        latitudeData = this.findViewById(R.id.latitudeData)
        longitudeData = this.findViewById(R.id.longitudeData)
        helloText = this.findViewById(R.id.helloText)

        // create Location Request
        createLocationRequest()

        queue = Volley.newRequestQueue(this)

//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(mLocationRequest)
//        val client: SettingsClient = LocationServices.getSettingsClient(this)
//        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        // Get Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.i("location", "fusedLocationClient created")

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    onLocationChanged(location)
                }
            }
        }
        checkPermission()
        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.getMainLooper())

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Get shared pref
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return

        sharedPref.getString(getString(R.string.saved_url),"")?.let { homeViewModel.updatePopLocation(it,queue)}

        registerSensors()
        checkName()
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()

        registerSensors()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)

        unRegisterSensors()
    }

    override fun onDestroy() {
        super.onDestroy()

        unRegisterSensors()
    }


    private fun registerSensors() {
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun unRegisterSensors() {
        sensorManager.unregisterListener(this, stepSensor)
    }

    private fun createLocationRequest(){
        mLocationRequest =  LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun startLocationUpdates() {
        if (checkPermission()) {
            fusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    @SuppressLint("SetTextI18n")
    fun onLocationChanged(location: Location) {
        Log.d("location","New ${location.latitude}+${location.longitude}")
        if (mLocation != null) {
            val result = FloatArray(1)
            mLocation?.let {
                Location.distanceBetween(
                    it.latitude,
                    it.longitude,
                    location.latitude,
                    location.longitude,
                    result
                )
            }
            if (result[0] >= 2) {
                distance += result[0]
                mLocation = location
            }

            if (tripLock)
                uploadData()

            Log.d("location","Distance-$distance")
            Log.d("location","Result-${result[0]}")
        } else {
            mLocation = location
            getAdvise(location)
        }

//        distanceData.text = "$distance m"
//        latitudeData.text = location.latitude.toString()
//        longitudeData.text = location.longitude.toString()

        homeViewModel.latitude.value = location.latitude
        homeViewModel.longtitude.value = location.longitude
        dashboardViewModel.distance.value = distance

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }

    private fun checkPermission(): Boolean {
        when {
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACTIVITY_RECOGNITION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                return true
            }
            else -> {
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION).forEach {
                    requestPermissionLauncher.launch(it)
                }
                return false
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event != null) {
            if (initSteps == 0f)
                initSteps = event.values[0]
//            stepData.text = (event.values[0]-initSteps).toString()
            dashboardViewModel.step.value = (event.values[0]-initSteps).toInt()
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    @SuppressLint("SetTextI18n")
    private fun checkName(){
        val userName = sharedPref.getString(getString(R.string.saved_user_name),"")!!
        if (userName == "")
            popNameSetting()
        else {
//            helloText.text = "Hello! $userName"
            homeViewModel.name.value = "Hello! $userName"
        }
    }

    private fun popNameSetting() {
        Snackbar.make(findViewById(R.id.container), R.string.no_name_pop, Snackbar.LENGTH_LONG).show()
    }

    private fun getAdvise(location: Location) {
        val url = "http://" + sharedPref.getString(getString(R.string.saved_url),"")!! + ":8082/adviser/info?latitude=${location.latitude}&longitude=${location.longitude}"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, null,
            { response ->
                Log.d("API",response.get("air").toString())
                homeViewModel.advise.value = response
            },
            { error ->
                Log.d("API", "error => $error")
            })

        queue.add(jsonObjectRequest)

    }

    fun saveAction(view: View){
        urlText = this.findViewById(R.id.urlText)
        nameText = this.findViewById(R.id.nameText)
        with(sharedPref.edit()) {
            putString(getString(R.string.saved_user_name), nameText.text.toString())
            putString(getString(R.string.saved_url), urlText.text.toString())
            apply()
        }
        mLocation?.let { getAdvise(it) }
        Snackbar.make(findViewById(R.id.container), R.string.saved_pop, Snackbar.LENGTH_SHORT).show()
        checkName()
    }

    private fun uploadData(){
        val url = "http://" + sharedPref.getString(getString(R.string.saved_url),"")!! + ":8182/producer/runningData"
        val userName = sharedPref.getString(getString(R.string.saved_user_name),"")!!
        val jsonBody = JSONObject()
        jsonBody.put("longitude", mLocation?.longitude.toString())
        jsonBody.put("latitude", mLocation?.latitude.toString())
        jsonBody.put("userId", userName)
        jsonBody.put("tripId", tripId)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url,jsonBody,
            { response ->
                Log.d("API", response.toString())
            },
            { error ->
                Log.d("API", "error => $error")
            })

        queue.add(jsonObjectRequest)
    }

    fun clickToUpload(view: View) {
        uploadData()
        Snackbar.make(findViewById(R.id.container), R.string.send, Snackbar.LENGTH_SHORT).show()
    }

}