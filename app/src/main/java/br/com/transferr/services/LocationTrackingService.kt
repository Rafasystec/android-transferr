package br.com.transferr.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.util.Log
import org.jetbrains.anko.doAsync

class LocationTrackingService : Service() {

    var locationManager: LocationManager? = null

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    val TAG = "LocationTrackingService"
    @SuppressLint("MissingPermission")
    override fun onCreate() {
        if (locationManager == null)
            locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        doAsync { run() }


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    private fun run(){
        var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        Log.d(TAG,"Location in  onCreated ${location?.latitude} - ${location?.longitude}")
        while (true){
            Thread.sleep(2*1000)
            location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            Log.d(TAG,"Location in  onCreated ${location?.latitude} - ${location?.longitude}")
        }

    }

}