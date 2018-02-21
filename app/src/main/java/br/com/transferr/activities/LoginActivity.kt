package br.com.transferr.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import br.com.transferr.R
import br.com.transferr.extensions.toast
import br.com.transferr.model.Credentials
import br.com.transferr.model.responses.ResponseLogin
import br.com.transferr.util.Prefes
import br.com.transferr.webservices.UserService
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginActivity : SuperClassActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnRegister.setOnClickListener{
            callService()
        }
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
           var loginResponse = ResponseLogin()
           var msgError      = ""
           doAsync {
               this@LoginActivity.runOnUiThread({
                   progressBar.visibility = View.VISIBLE
               })
               try {
                    loginResponse = UserService.doLogin(getCredentialsFromForm())
               }catch (e:Exception){
                   msgError = e.message!!
                   this@LoginActivity.runOnUiThread({
                       progressBar.visibility = View.GONE
                   })
               }
               uiThread {
                   this@LoginActivity.runOnUiThread({
                       progressBar.visibility = View.GONE
                   })
                   if(!msgError.isEmpty()){
                       toast("Erro ao tentar logar: $msgError")
                   }
                   if(loginResponse != null){
                       if(loginResponse.user != null){
                           val id = loginResponse.user!!.id!!
                           if(id > 0){
                                executeLogin(id)
                           }
                       }
                   }
               }

           }

       }

    }

    private fun checkUserLogin(){
        val id = Prefes.prefsLogin
        if(id != null && id > 0L){
            callMainActivity()
        }
    }

    private fun callMainActivity(){
        startActivity(Intent(context,MainActivity::class.java))
    }

    private fun getCredentialsFromForm():Credentials{
        return Credentials(txtLogin.text.toString(),txtPassword.text.toString())
    }

    private fun executeLogin(idUser:Long){
        Prefes.prefsLogin = idUser
        callMainActivity()
    }
}
