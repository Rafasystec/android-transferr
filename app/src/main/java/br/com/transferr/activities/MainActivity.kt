package br.com.transferr.activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.View
import br.com.transferr.R
import br.com.transferr.extensions.log
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.showError
import br.com.transferr.extensions.showValidation
import br.com.transferr.model.Car
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.RequestCoordinatesUpdate
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.services.LocationTrackingService
import br.com.transferr.util.NetworkUtil
import br.com.transferr.util.Prefes
import br.com.transferr.util.VariablesUtil
import br.com.transferr.webservices.CarService
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : SuperClassActivity() {

    lateinit var locationManager: LocationManager
    var car:Car?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar,"Início")
        getBundleValues(savedInstanceState)
        checkLogin()
        initView()
    }
    private fun initView(){
        btnFrmDriver.setOnClickListener { callFormDriver() }
        swtOnline.setOnClickListener { stopInitLocation() }
        checkNetwork()

    }

    private fun callFormDriver(){
        val intentInit = Intent(context,DriverInforActivity::class.java)
        startActivity(intentInit)
    }

    private fun stopInitLocation(){
        if(swtOnline.isChecked){
            startService()
            swtOnline.setTextColor(Color.BLUE)
            onlineOffline(true)
        }else{
            stopServiceIntent()
            onlineOffline(false)
            swtOnline.setTextColor(Color.BLACK)
        }
    }

    private fun startService(){
        if(isLocationPermissionGranted()) {
            if(!isLocationEnabled()) {
                showAlert()
            }else{
                startServiceIntent()
            }

        }
        startService(Intent(this, LocationTrackingService::class.java))
    }

    private fun stopServiceIntent(){
        stopService(Intent(this,LocationTrackingService::class.java))
    }

    private fun startServiceIntent(){
        startService(Intent(this, LocationTrackingService::class.java))
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
                    startServiceIntent()
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                   swtOnline.isChecked = false
                   stopServiceIntent()
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

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putBoolean(VariablesUtil.ONLINE, swtOnline.isChecked)
    }

    private fun initScreenFields(car:Car){
        this.car = car
        Prefes.prefsCar = car.id!!
        lblColorValue.text = car.color
        lblDriverValue.text= car.driver?.name
        lblModelValue.text = car.model
        lblPlacaValue.text = car.carIdentity
    }

    private fun getCarFromWebService(){
        initProgressBar()
        CarService.getCarByUser(Prefes.prefsLogin,
            object: OnResponseInterface<Car>{
                    override fun onSuccess(car: Car?) {
                        stopProgressBar()
                        initScreenFields(car!!)
                        Prefes.prefsCarJSON = car
                    }

                    override fun onError(message: String) {
                        stopProgressBar()
                        toast(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        stopProgressBar()
                        toast("Erro ao logar ${t?.message}")
                    }

            }
        )

    }

    private fun checkUserLogin():Boolean{
        val id = Prefes.prefsLogin
        if(id == null || id <= 0){
            return false
        }
        return true
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initProgressBar(){
        this@MainActivity.runOnUiThread({
            progressBar.visibility = View.VISIBLE
        })
    }

    private fun stopProgressBar(){
        this@MainActivity.runOnUiThread({
            progressBar.visibility = View.GONE
        })
    }

    private fun checkLogin(){
        val isLoged = checkUserLogin()
        if(isLoged) {
            stopInitLocation()
            getCarFromWebService()
        }else{
           callLoginActivity()
        }
    }

    private fun callLoginActivity(){
        startActivity(Intent(context,LoginActivity::class.java))
        finish()
    }

    private fun onlineOffline(online:Boolean){
        var request = RequestCoordinatesUpdate()
        request.idCar = Prefes.prefsCar
        if(online) {
            //Pegar as coordenadas reais
            CarService.online(request,
                    object : OnResponseInterface<ResponseOK>{
                        override fun onSuccess(body: ResponseOK?) {
                            log("OK")
                        }

                        override fun onError(message: String) {
                            showValidation(message)
                        }

                        override fun onFailure(t: Throwable?) {
                            showError(t?.message!!)
                        }

                    }
            )
        }else{
            CarService.offline(request,
                    object : OnResponseInterface<ResponseOK>{
                        override fun onSuccess(body: ResponseOK?) {
                            log("OK- Estou offline")
                        }

                        override fun onError(message: String) {
                            showValidation(message)
                        }

                        override fun onFailure(t: Throwable?) {
                            showError(t)
                        }

                    }
            )
        }

    }
    private fun getBundleValues(savedInstanceState: Bundle?){
        if(savedInstanceState != null){
            swtOnline.isChecked = savedInstanceState.getBoolean(VariablesUtil.ONLINE)
        }
    }
    
}
