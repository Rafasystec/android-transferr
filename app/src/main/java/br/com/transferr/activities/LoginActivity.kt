package br.com.transferr.activities

import android.content.Intent
import android.os.Bundle
import br.com.transferr.R
import br.com.transferr.extensions.toast
import br.com.transferr.model.Credentials
import br.com.transferr.util.Prefes
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : SuperClassActivity() {
    var prefes: Prefes? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnRegister.setOnClickListener{
            val credentials = Credentials(txtLogin.text.toString(),txtPassword.text.toString())
            callService(credentials)
        }
        checkUserLogin()
    }

    private fun callService(credentials:Credentials){
        //toast("Seu usuário foi enviado ao servidor: ${credentials.login}",Toast.LENGTH_SHORT)
        prefes = Prefes(this)
        if(txtLogin.text.toString() == "rafael" && txtPassword.text.toString() == "123456"){
            toast("Login efetuado com sucesso!")
            prefes!!.prefsLogin = 1
            checkUserLogin()
        }else{
            toast("Falha no login.")
        }

    }

    private fun checkUserLogin(){
        prefes = Prefes(this)
        val id = prefes!!.prefsLogin
        if(id != 0){
            callMainActivity()
        }
    }

    private fun callMainActivity(){
        startActivity(Intent(context,MainActivity::class.java))
    }
}
