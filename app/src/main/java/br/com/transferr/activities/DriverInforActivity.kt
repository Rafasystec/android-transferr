package br.com.transferr.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.View
import br.com.transferr.R
import br.com.transferr.extensions.log
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.model.Driver
import br.com.transferr.util.ImageUtil
import br.com.transferr.webservices.DriverService
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlinx.android.synthetic.main.activity_driver_infor.*
import java.io.File

class DriverInforActivity : SuperClassActivity() {

    var file:File? = null
    var driverWebService:DriverService = DriverService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_infor)
        btnAlterPass.setOnClickListener { callRestAlterPassword() }
        getDriverFromWebService()
        setupToolbar(R.id.toolbar,"Meus Dados",true)
        btnCamera.setOnClickListener {
            file = getSdCardFile("photoProfile.jpg")
            log("Camera file $file")
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val uri = FileProvider.getUriForFile(context,context.applicationContext.packageName+".provider",file)
            i.putExtra(MediaStore.EXTRA_OUTPUT,uri)
            startActivityForResult(i,0)
        }
        if(savedInstanceState != null){
            //Se girou a tela recuper ao estado
            file = savedInstanceState.getSerializable("file") as File
            showImage(file)
        }
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

    private fun getSdCardFile(fileName:String): File{
        val sdCardDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if(!sdCardDir.exists()){
            sdCardDir.mkdir()
        }
        return File(sdCardDir,fileName)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("file",file)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        log("result code: $resultCode")
        if(resultCode == Activity.RESULT_OK){
            //Se a camera retornou vamos mostar o arquivo da foto
            showImage(file)
        }
    }

    private fun showImage(file: File?){
        if(file != null && file.exists()){
            val w = imgProfile.width
            val h = imgProfile.height
            //Redimenciona a imagem para o tamnho do imageview
            val bitmap = ImageUtil.resize(file,w,h)
            imgProfile.setImageBitmap(bitmap)
        }
    }
}
