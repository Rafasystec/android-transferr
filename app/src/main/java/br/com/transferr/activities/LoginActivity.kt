package br.com.transferr.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import br.com.transferr.R
import br.com.transferr.extensions.getTextAsString
import br.com.transferr.extensions.toast
import br.com.transferr.model.Credentials

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val txtLogin    = getTextAsString(R.id.txtLogin)
        val txtPassword = getTextAsString(R.id.txtPassword)
        val btnLogin    = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener{
            val credentials = Credentials(txtLogin,txtPassword)
            callService(credentials)
        }
    }

    private fun callService(credentials:Credentials){
        toast("Seu usuário foi enviado ao servidor: ${credentials.login}",Toast.LENGTH_SHORT)
    }
}
