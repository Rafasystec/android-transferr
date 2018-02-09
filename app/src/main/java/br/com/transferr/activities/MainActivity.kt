package br.com.transferr.activities

import android.content.Intent
import android.os.Bundle
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : SuperClassActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar)
        //btnLocation.setOnClickListener{callInitialActivity()}
        swtOnline.setOnClickListener { stopInitLocation() }
    }

    fun callInitialActivity(){
        val intentInit = Intent(context,InitialActivity::class.java)
        startActivity(intentInit)
    }

    private fun stopInitLocation(){
        if(swtOnline.isChecked){
            toast("Vc agoa esta online")
        }else{
            toast("Vc esta off")
        }
    }
}
