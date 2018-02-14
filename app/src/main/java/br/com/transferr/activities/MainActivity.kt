package br.com.transferr.activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.model.Car
import br.com.transferr.services.LocationTrackingService
import br.com.transferr.util.Prefes
import br.com.transferr.webservices.CarService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : SuperClassActivity() , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private val TAG = "INITIAL_ACTIVITY"
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    override fun onLocationChanged(location: Location) {
        var msg = "Updated Location: LatLon " + location.latitude + " - " + location.longitude
        //txt_latitude.setText(""+location.latitude)
        //txt_longitude.setText(""+location.longitude)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onConnected(p0: Bundle?) {
        if (!isLocationPermissionGranted()) {
            return
        }
        stopInitLocation()
    }
    var prefes: Prefes? = null

    var carService:CarService = CarService()
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    lateinit var mLocation: Location
    private var mLocationRequest: LocationRequest? = null
    private val listener: com.google.android.gms.location.LocationListener? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar,"Início")
        btnFrmDriver.setOnClickListener { callFormDriver() }
        swtOnline.setOnClickListener { stopInitLocation() }
        buildLocationAPI()
        val isLoged = checkUserLogin()
        if(isLoged) {
            stopInitLocation()
            getCarFromWebService()
        }else{
            startActivity(Intent(context,LoginActivity::class.java))
        }
    }

    private fun callFormDriver(){
        val intentInit = Intent(context,DriverInforActivity::class.java)
        startActivity(intentInit)
    }

    private fun stopInitLocation(){
        if(swtOnline.isChecked){
            startService()
            swtOnline.setTextColor(Color.BLUE)
        }else{
            toast("Vc esta off 7")
            stopService()
            swtOnline.setTextColor(Color.BLACK)
        }
    }

    private fun startService(){
        if(isLocationPermissionGranted()) {
            if(!isLocationEnabled()) {
                showAlert()
            }
            toast("Vc agora esta online")
            startService(Intent(this, LocationTrackingService::class.java))
        }
        toast("Vc agora esta online")
        startService(Intent(this, LocationTrackingService::class.java))
    }

    private fun stopService(){
        stopService(Intent(this,LocationTrackingService::class.java))
    }

    private fun buildLocationAPI(){
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()
    }

    private fun checkLocation(): Boolean {
        if(!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient.connect()
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Habilitar Localização")
                .setMessage("Precisamos ativar o GPS.\nPor favor ative-o.")
                .setPositiveButton("Ativar GPS", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                    swtOnline.isChecked = true
                    stopInitLocation()
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                   swtOnline.isChecked = false
                   stopService()
                })
        dialog.show()
    }

    private fun isLocationPermissionGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    private fun initScreenFields(car:Car){
        lblColorValue.text = car.color
        lblDriverValue.text= car.driver.name
        lblModelValue.text = car.model
        lblPlacaValue.text = car.carIdentity
    }

    private fun getCarFromWebService(): Unit{
        doAsync {
            this@MainActivity.runOnUiThread({
                progressBar.visibility = View.VISIBLE
            })
            var car = carService.getCar(1)
            uiThread {
                initScreenFields(car)
                this@MainActivity.runOnUiThread({
                    progressBar.visibility = View.GONE
                })
            }
        }
    }

    private fun checkUserLogin():Boolean{
        prefes = Prefes(this)
        val id = prefes!!.prefsLogin
        if(id == 0){
            return false
        }
        return true
    }


}
