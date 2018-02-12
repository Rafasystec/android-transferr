package br.com.transferr.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.util.Log
import br.com.transferr.model.Car
import br.com.transferr.model.Coordinates
import br.com.transferr.model.Driver
import br.com.transferr.model.enums.EnumStatus
import br.com.transferr.webservices.CoordinateService
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class LocationTrackingService : Service() {

    private var locationManager: LocationManager? = null

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        toast("starting service")
        runs=true
        return START_STICKY
    }

    val TAG = "LocationTrackingService"
    @SuppressLint("MissingPermission")
    override fun onCreate() {
        if (locationManager == null)
            locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.d(TAG,"on create")
        runs = true
        doAsync { run() }


    }

    override fun onDestroy() {
        super.onDestroy()
        toast("destroying the service 1")
        runs=false
    }

    companion object {

        var runs = true
    }

    @SuppressLint("MissingPermission")
    private fun run(){
        //var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        //var location = getLastKnownLocation()
        //Log.d(TAG,"Location run ${location?.latitude} - ${location?.longitude}")

        while (runs){
            Thread.sleep(2*1000)
            var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(location != null) {
                Log.d(TAG, "My Location ${location?.latitude} - ${location?.longitude}")
                callWebService(location)
            }else{
                Log.d(TAG, "Cannot get the location, it was null.")
            }
        }

    }

    private fun callWebService(location: Location?) {
        var car: Car = getDefaultCar()
        var coordinates = Coordinates()
        coordinates.id = 0
        coordinates.car         = car
        coordinates.latitude    = location?.latitude
        coordinates.longitude   = location?.longitude
        doAsync {
            val response = CoordinateService.save(coordinates)
            uiThread {
                var msg = response.msg
                if(msg == null){
                    msg = "Invalid Message"
                }
                toast(msg)
                //finish()
            }
        }
    }

    private fun getDefaultCar(): Car {
        var driver: Driver = Driver("Jose","664764", 19880305)
        var car:Car = Car("cheve","2215563","azul",true,driver, EnumStatus.OFFLINE)
        return car
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager!!.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l = locationManager!!.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.getAccuracy() < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        return bestLocation
    }

}