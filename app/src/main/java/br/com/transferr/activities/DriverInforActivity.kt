package br.com.transferr.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.transferr.R
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.extensions.*
import br.com.transferr.helpers.HelperCamera
import br.com.transferr.model.AnexoPhoto
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.util.ImageUtil
import br.com.transferr.util.Prefes
import br.com.transferr.webservices.DriverService
import br.com.transferr.webservices.UserService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_driver_infor.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class DriverInforActivity : SuperClassActivity() {
    private val camera      = HelperCamera()
    private val photoName   = "photoProfile.jpg"
    var file:File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_infor)
        setupToolbar(R.id.toolbar,"Meus Dados",true)
        initViews()
        camera.init(savedInstanceState)
    }

    private fun initScreenFields(driver:Driver){
        lblDriverNameValue.text     = driver.name
        lblCfpValue.text            = driver.countryRegister
        lblDtNascimentoValue.text   = driver.birthDate.toString()
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
        btnAlterPass.setOnClickListener{btnAlterPassClick()}
        getDriver()
        loadPhoto(Prefes.ID_CAR)
    }

    private fun loadPhoto(idCar:String){
        var url = ApplicationTransferr.getInstance().URL_BASE_IMG + "/car/$idCar"+"/$photoName"
        Picasso.with(this).load(url).into(imgProfile)
    }

    private fun btnCameraClick(){
        startActivityForResult(camera.open(this,photoName),0)
    }

    private fun btnAlterPassClick(){
        if(validate()){
            callServiceToChangeThePassword()
        }
    }

    private fun validate():Boolean{
        val oldPassword = txtOldPassword.text.toString().trim()
        val newPassword = txtNewPassword.text.toString().trim()
        val confPassword= txtConfirmNewPassword.text.toString().trim()
        if(oldPassword.isEmpty()){
            toast("Por favor informe a sua senha antiga!")
            return false
        }else if(newPassword.isEmpty()){
            toast("Por favor informe a nova senha!")
            return false
        }else if(confPassword.isEmpty()){
            toast("Por favor confirme a senha!")
            return false
        }else{
            if(oldPassword == newPassword){
                toast("Por favor informe uma senha diferente da sua antiga")
                return false
            }
            if(newPassword != confPassword){
                toast("As senhas não conferem!")
                return false
            }
        }
        return true
    }

    private fun callServiceToChangeThePassword(){
        doAsync {
            var message = ""
            var response:ResponseOK? = null
            try {
                response = UserService.changePassword(Prefes.prefsLogin,
                        txtOldPassword.text.toString(),
                        txtNewPassword.text.toString())
            }catch (e:Exception){
                message = e.message!!
                toast("Erro ao tentar alterar a senha ${e.message}")
            }
            uiThread {
                if(response != null){
                    toast("Senha alterada com sucesso!")
                }
            }
        }
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

    private fun initProgressBar(){
        this@DriverInforActivity.runOnUiThread({
            progressBar.visibility = View.VISIBLE
        })
    }

    private fun stopProgressBar(){
        this@DriverInforActivity.runOnUiThread({
            progressBar.visibility = View.GONE
        })
    }


    private fun getDriver(){
        initProgressBar()
        DriverService.getDriverByCar(Prefes.prefsCar,
                object : OnResponseInterface<Driver>{
                    override fun onSuccess(driver: Driver?) {
                        stopProgressBar()
                        initScreenFields(driver!!)
                    }

                    override fun onError(message: String) {
                        stopProgressBar()
                        showValidation(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        stopProgressBar()
                        showError(t?.message!!)
                    }

                })
    }
/*
    private fun getDriver(){
        initProgressBar()
        //var car = intent.getParcelableExtra<Entity>(VariablesUtil.MY_CAR)
        //intent.getSerializableExtr
        val bundle = intent.getBundleExtra("bundle")
        var car  = bundle.getParcelable<Car>(VariablesUtil.MY_CAR) as Car
        initScreenFields(car.driver!!)
        stopProgressBar()
    }
    */
}
