package br.com.transferr.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import br.com.transferr.R
import br.com.transferr.util.Prefes
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast

/**
 * Created by root on 06/02/18.
 */
@SuppressLint("Registered")
open class SuperClassActivity : AppCompatActivity(){
    //Propriedade para acessar o contexto de qualquer lugar
    protected val context: Context get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG","setSupportActionBar")
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        Log.d("DEBUG","Creating default menu for all activities")
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_exit -> {
            // User chose the "Settings" item, show the app settings UI...
            logout()
            callLoginActivity()
            true
        }

        R.id.action_mapa -> {
            // User chose the "Favorite" action, mark the current item
            startMap()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun logout(){
        Prefes.prefsLogin = 0
    }

    private fun callLoginActivity(){
        startActivity(Intent(context,LoginActivity::class.java))
    }

    private fun startMap(){
        startActivity(Intent(context,InitialActivity::class.java))
    }


}