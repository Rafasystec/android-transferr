package br.com.transferr.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.transferr.R
import br.com.transferr.extensions.toast
import br.com.transferr.model.Credentials
import br.com.transferr.model.responses.ResponseLogin
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.util.Prefes
import br.com.transferr.util.RestUtil
import br.com.transferr.webservices.UserService
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

class LoginActivity : SuperClassActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnRegister.setOnClickListener{
            callService()
        }
        btnRecoverPass.setOnClickListener{
            callServiceToRecoverPassword()
        }
        toast("onCreate login")
        checkUserLogin()
    }

    private fun validate() : Boolean{
        if(txtLogin.text.toString().trim().isEmpty()){
            toast("Por favor informe o usuário")
            return false
        }
        if(txtPassword.text.toString().trim().isEmpty()){
            toast("Por favor informe a senha!")
            return false
        }

        return true
    }

    private fun callService(){
       if(validate()){
            initProgressBar()
           UserService.getService().doLogin(getCredentialsFromForm()).enqueue(
                   object : Callback<ResponseLogin> {
                       override fun onResponse(call: Call<ResponseLogin>?, response: Response<ResponseLogin>?) {
                           stopProgressBar()
                            if(response != null && response.isSuccessful){
                                response?.let {
                                    executeLogin(it.body()?.user?.id!!)
                                }
                            }else if(response != null){
                               toast(RestUtil<ResponseLogin>().getErroMessage(response))
                            }
                       }

                       override fun onFailure(call: Call<ResponseLogin>?, t: Throwable?) {
                           stopProgressBar()
                            toast("Erro ao logar ${t?.message}")
                       }



                   }
           )

       }

    }

    private fun checkUserLogin(){
        val id = Prefes.prefsLogin
        if(id != null && id > 0L){
            callMainActivity()
            finish()
        }
    }

    private fun callMainActivity(){
        startActivity(Intent(context,MainActivity::class.java))
        finish()
    }

    private fun getCredentialsFromForm():Credentials{
        return Credentials(txtLogin.text.toString(),txtPassword.text.toString())
    }

    private fun executeLogin(idUser:Long){
        Prefes.prefsLogin = idUser
        callMainActivity()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        toast("onBackPressed login")
        exitProcess(0)
        finish()
    }

    private fun callServiceToRecoverPassword(){
        var email = txtLogin.text.toString().trim()
        if(email.isEmpty()){
            toast("Por favor informe o Usuário, ele é o seu e-mail.")
        }else {

            UserService.getService().recoverPassword(email).enqueue(
                    object : Callback<ResponseOK> {
                        override fun onFailure(call: Call<ResponseOK>?, t: Throwable?) {
                            toast("Erro grave ${t?.message}")
                        }

                        override fun onResponse(call: Call<ResponseOK>?, response: Response<ResponseOK>?) {
                           if(response?.isSuccessful!!){
                               toast("Um e-mail com a sua senha foi enviado para $email")
                           }else if(response != null){
                                toast(RestUtil<ResponseOK>().getErroMessage(response))
                           }
                        }

                    }
            )
        }
    }

    private fun initProgressBar(){
        this@LoginActivity.runOnUiThread({
            progressBar.visibility = View.VISIBLE
        })
    }

    private fun stopProgressBar(){
        this@LoginActivity.runOnUiThread({
            progressBar.visibility = View.GONE
        })
    }
}


