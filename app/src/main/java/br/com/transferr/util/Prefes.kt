package br.com.transferr.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import br.com.transferr.application.ApplicationTransferr

/**
 * Created by root on 12/02/18.
 */
object Prefes {
    val PREFS_FILENAME      = "br.com.transferr.prefs"
    val ID_DRIVER           = "br.com.transferr.driver.id"
    //val DRIVER              = "br.com.transferr.driver"
    //val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    private fun prefs() : SharedPreferences{
        val contex = ApplicationTransferr.getInstance().applicationContext
        return contex.getSharedPreferences(PREFS_FILENAME,0)
    }
    var prefsLogin: Int
        get() = prefs().getInt(ID_DRIVER,0)
        set(value) = prefs().edit().putInt(ID_DRIVER, value).apply()
    //Use to save other values
    fun setInt(flag:String,value:Int) = prefs().edit().putInt(flag,value).apply()
    fun getInt(flag: String)= prefs().getInt(flag,0)
    fun setString(flag: String,value:String) = prefs().edit().putString(flag,value).apply()

}