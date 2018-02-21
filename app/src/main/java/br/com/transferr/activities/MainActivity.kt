package br.com.transferr.activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.View
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.toast
import br.com.transferr.model.Car
import br.com.transferr.model.Driver
import br.com.transferr.model.enums.EnumStatus
import br.com.transferr.services.LocationTrackingService
import br.com.transferr.util.NetworkUtil
import br.com.transferr.util.Prefes
import br.com.transferr.webservices.CarService
import kotlinx.android.synthetic.main.action_map.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : SuperClassActivity() {

    lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar,"Início")
        btnFrmDriver.setOnClickListener { callFormDriver() }
        swtOnline.setOnClickListener { stopInitLocation() }
        addActionToFloatingButtonMap()
        val isLoged = checkUserLogin()
        checkNetwork()
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
            //toast("Vc esta off 7")
            stopService()
            swtOnline.setTextColor(Color.BLACK)
        }
    }

    private fun startService(){
        if(isLocationPermissionGranted()) {
            if(!isLocationEnabled()) {
                showAlert()
            }
            startService(Intent(this, LocationTrackingService::class.java))
        }
        startService(Intent(this, LocationTrackingService::class.java))
    }

    private fun stopService(){
        stopService(Intent(this,LocationTrackingService::class.java))
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

    private fun getCarFromWebService(){
        doAsync {
            this@MainActivity.runOnUiThread({
                progressBar.visibility = View.VISIBLE
            })
            var car:Car? = getEmptyCar()
            var errorMessage= ""
            if(isConnected()) {
                try {
                    car = CarService.getCar(1)
                    Prefes.prefsCar = car.id!!
                }catch (e:Exception){
                    errorMessage = e.message!!
                    this@MainActivity.runOnUiThread({
                        progressBar.visibility = View.GONE
                    })
                }
            }
            uiThread {
                if(!errorMessage.isEmpty()){
                    toast(errorMessage)
                }
                if(isConnected()) {
                    initScreenFields(car!!)
                    this@MainActivity.runOnUiThread({
                        progressBar.visibility = View.GONE
                    })
                }else{
                    this@MainActivity.runOnUiThread({
                        progressBar.visibility = View.GONE
                    })
                }
            }
        }
    }

    private fun checkUserLogin():Boolean{
        val id = Prefes.prefsLogin
        val idZero:Long = 0
        if(id == null || id <= 0){
            return false
        }
        return true
    }

    private fun addActionToFloatingButtonMap(){
        actionMap.setOnClickListener {
            startMap()
        }
    }

    private fun startMap(){
        startActivity(Intent(context,InitialActivity::class.java))
    }

    private fun checkNetwork(){
        if(!isConnected()){
            toast("Sem internet. Por favor ative sua internet")
        }
    }

    private fun isConnected():Boolean{
        return NetworkUtil.isNetworkAvailable(this)
    }

    private fun getEmptyCar():Car{
        return Car("","","",false,
                Driver("","",0),EnumStatus.OFFLINE)
    }

}
