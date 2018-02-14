package br.com.transferr.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.model.Driver
import br.com.transferr.webservices.DriverService
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlinx.android.synthetic.main.activity_driver_infor.*

class DriverInforActivity : SuperClassActivity() {

    var driverWebService:DriverService = DriverService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_infor)
        btnAlterPass.setOnClickListener { callRestAlterPassword() }
        getDriverFromWebService()
        setupToolbar(R.id.toolbar,"Meus Dados",true)
        //supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initScreenFields(driver:Driver){
        lblDriverNameValue.text     = driver.name
        lblCfpValue.text            = driver.countryRegister
        lblDtNascimentoValue.text   = driver.birthDate.toString()
    }

    private fun getDriverFromWebService(){
        doAsync {
            this@DriverInforActivity.runOnUiThread({
                progressBar.visibility = View.VISIBLE
            })
            var driver = driverWebService.getDriver(1)
            uiThread {
                initScreenFields(driver)
                this@DriverInforActivity.runOnUiThread({
                    progressBar.visibility = View.GONE
                })
            }
        }

    }

    private fun callRestAlterPassword(){

    }
}
