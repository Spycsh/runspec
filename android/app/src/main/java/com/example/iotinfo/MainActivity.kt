package com.example.iotinfo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private var requestingLocationUpdates = true
    private lateinit var locationCallback: LocationCallback

    private lateinit var latitudeData: TextView
    private lateinit var longitudeData: TextView
    private lateinit var stepData: TextView

    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor

    private var initSteps = 0f
    private var stepsTaken = 0
    private var reportedSteps = 0

//    private val REQUEST_FINE_LOCATION = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get text views
        latitudeData = this.findViewById(R.id.latitudeData)
        longitudeData = this.findViewById(R.id.longitudeData)
        stepData = this.findViewById(R.id.stepData)

        // create Location Request
        createLocationRequest()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

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

        registerSensors()
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
            interval = 1000
            fastestInterval = 500
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

    fun onLocationChanged(location: Location) {
        latitudeData.text = location.latitude.toString()
        longitudeData.text = location.longitude.toString()
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
            stepData.text = (event.values[0]-initSteps).toString()
        }

//        val sensor = event?.sensor
//
//        when (event!!.sensor.type) {
//            Sensor.TYPE_STEP_COUNTER -> {
//                if (reportedSteps < 1) {
//                    // Log the initial value
//                    reportedSteps = event.values[0].toInt()
//                }
//
//                // Calculate steps taken based on
//                // first value received.
//                stepsTaken = event.values[0].toInt() - reportedSteps
//
//                // Output the value to the simple GUI
//                stepData.text = stepsTaken.toString()
//            }
//        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}