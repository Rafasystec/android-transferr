package br.com.transferr.activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.model.Car
import br.com.transferr.services.LocationTrackingService
import br.com.transferr.util.Prefes
import br.com.transferr.webservices.CarService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : SuperClassActivity() , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{


    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onLocationChanged(p0: Location?) {
    }

    override fun onConnected(p0: Bundle?) {
        if (!isLocationPermissionGranted()) {
            return
        }
        stopInitLocation()
    }

    var carService:CarService = CarService()
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar)
        btnFrmDriver.setOnClickListener { callFormDriver() }
        swtOnline.setOnClickListener { stopInitLocation() }
        buildLocationAPI()
        stopInitLocation()
        getCarFromWebService()
    }
/*
    fun callInitialActivity(){
        val intentInit = Intent(context,InitialActivity::class.java)
        startActivity(intentInit)
    }
*/
    private fun callFormDriver(){
        val intentInit = Intent(context,DriverInforActivity::class.java)
        startActivity(intentInit)
    }

    private fun stopInitLocation(){
        if(swtOnline.isChecked){
            startService()
        }else{
            toast("Vc esta off 7")
            stopService()
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
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient.isConnected()) {
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
        //var car = getCarFromWebService()
        lblColorValue.text = car.color
        lblDriverValue.text= car.driver.name
        lblModelValue.text = car.model
        lblPlacaValue.text = car.carIdentity
    }

    private fun getCarFromWebService(): Unit{
        //var driver = Driver("Dalessandro Vieira","Brazil",19800303)
        //return Car("S-10","HHy-1356","Cinza fosco",false,driver,EnumStatus.OFFLINE)
        doAsync {
            var car = carService.getCar(1)
            uiThread {
                initScreenFields(car)
            }
        }
    }


}
