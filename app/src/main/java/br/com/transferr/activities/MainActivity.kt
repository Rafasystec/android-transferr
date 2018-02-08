package br.com.transferr.activities

import android.content.Intent
import android.os.Bundle
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : SuperClassActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar)
        btnLocation.setOnClickListener{callInitialActivity()}
    }

    fun callInitialActivity(){
        val intentInit = Intent(context,InitialActivity::class.java)
        startActivity(intentInit)
    }
}
