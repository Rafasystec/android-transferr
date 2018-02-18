package br.com.transferr.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import br.com.transferr.model.Car
import br.com.transferr.model.Coordinates
import br.com.transferr.model.Driver
import br.com.transferr.model.enums.EnumStatus
import br.com.transferr.util.MyLocationLister
import br.com.transferr.util.NetworkUtil
import br.com.transferr.webservices.CoordinateService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.util.*

class LocationTrackingService : Service(),com.google.android.gms.location.LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "Connection failed. Error: " + connectionResult.errorCode)
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        Log.d(TAG,"onConnected call")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        startLocationUpdates()
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient.connect()
    }

    override fun onLocationChanged(location: Location?) {
        var msg = "Updated Location: LatLon " + location?.latitude + " - " + location?.longitude
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        callWebService(location)
    }

    //private var locationManager: LocationManager?   = null
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationRequest: LocationRequest?  = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private var mLocationManager: LocationManager? = null

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        //toast("starting service")
        //runs=true
        return START_STICKY
    }

    private val TAG = "LocationTrackingService"
    @SuppressLint("MissingPermission")
    override fun onCreate() {
        Log.d(TAG,"on create")
        buildLocationAPI()
    }

    /*
    @SuppressLint("MissingPermission")
    private fun run(){

        while (runs){
            Thread.sleep(3*1000)
            var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(location != null) {
                Log.d(TAG, "***********My Location ${location?.latitude} - ${location?.longitude}")
                callWebService(location)
            }else{
                Log.d(TAG, "Cannot get the location, it was null.")
            }
        }


    }
*/
    private fun callWebService(location: Location?) {
        var car: Car = getDefaultCar()
        var coordinates = Coordinates()
        coordinates.id = 0
        coordinates.car         = car
        coordinates.latitude    = location?.latitude
        coordinates.longitude   = location?.longitude
        doAsync {
            if(isConnected()) {
                CoordinateService.save(coordinates)
            }
        }
    }

    private fun getDefaultCar(): Car {
        var driver: Driver = Driver("Jose","664764", 19880305)
        var car:Car = Car("cheve","2215563","azul",true,driver, EnumStatus.OFFLINE)
        return car
    }

    @SuppressLint("MissingPermission")
    private fun buildLocationAPI(){
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient.connect()
        mLocationRequest = LocationRequest()
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(location != null) {
            Log.d(TAG, "My Location on  buildLocationAPI method ${location?.latitude} - ${location?.longitude}")
            callWebService(location)
        }else{
            Log.d(TAG, "Cannot get the location o build API, it was null.")
        }

    }


    private fun startLocationUpdates() {

        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this)

    }

    private fun isConnected():Boolean{
        return NetworkUtil.isNetworkAvailable(this)
    }

}