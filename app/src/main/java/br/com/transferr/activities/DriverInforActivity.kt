package br.com.transferr.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.transferr.R
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.extensions.log
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.toast
import br.com.transferr.helpers.HelperCamera
import br.com.transferr.model.AnexoPhoto
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.util.ImageUtil
import br.com.transferr.util.Prefes
import br.com.transferr.webservices.DriverService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_driver_infor.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class DriverInforActivity : SuperClassActivity() {
    val camera = HelperCamera()
    val photoName = "photoProfile.jpg"
    var file:File? = null
    var driver = Driver("","",0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_infor)
        btnAlterPass.setOnClickListener { callRestAlterPassword() }
        getDriverFromWebService()
        setupToolbar(R.id.toolbar,"Meus Dados",true)
        initViews()
        camera.init(savedInstanceState)
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
                //layoutMain.visibility = View.GONE
            })
            driver = DriverService.getDriver(1)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putSerializable("file",file)
        camera.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        log("result code: $resultCode")
        if(resultCode == Activity.RESULT_OK){
            //Se a camera retornou vamos mostar o arquivo da foto
            val bitmap = camera.getBitmap(600,600)
            if(bitmap != null) {
                camera.save(bitmap)
                showImage(camera.file)
                postImageProfile()
            }
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

    private fun initViews(){
        btnCamera.setOnClickListener{btnCameraClick()}
        loadPhoto(Prefes.ID_CAR)
    }

    private fun loadPhoto(idCar:String){
        var url = ApplicationTransferr.getInstance().URL_BASE_IMG + "/car/$idCar"+"/$photoName"
        Picasso.with(this).load(url).into(imgProfile)
    }

    private fun btnCameraClick(){
        startActivityForResult(camera.open(this,photoName),0)
    }

   // val dialog = ProgressDialog.show(this,"Salvando","Salvando imagem. Aguarde.",false,true)
    private fun postImageProfile(){
        var anexoPhoto = AnexoPhoto()
        doAsync {
            var response:ResponseOK? = null
            var msgError:String? = ""
            try {
                response = DriverService.savePhoto(anexoPhoto)
            }catch (e:Exception){
                msgError = e.message
            }
            uiThread {
                if(response != null) {
                    toast("Imagem salva")
                }else{
                    toast("Erro ao tentar salvar a imagem $msgError")
                }
                //dialog.dismiss()
                finish()
            }
        }

    }
}
