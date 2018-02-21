package br.com.transferr.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import br.com.transferr.application.ApplicationTransferr

/**
 * Created by root on 12/02/18.
 */
object Prefes {
    val PREFS_FILENAME   = "br.com.transferr.prefs"
    val ID_DRIVER        = "br.com.transferr.driver.id"
    val ID_USER          = "br.com.transferr.user.id"
    val ID_CAR           = "br.com.transferr.car.id"

    private fun prefs() : SharedPreferences{
        val contex = ApplicationTransferr.getInstance().applicationContext
        return contex.getSharedPreferences(PREFS_FILENAME,0)
    }
    var prefsLogin: Long
        get() = prefs().getLong(ID_USER,0L)
        set(value) = prefs().edit().putLong(ID_USER, value).apply()
    var prefsDriver: Long
        get() = prefs().getLong(ID_DRIVER,0L)
        set(value) = prefs().edit().putLong(ID_DRIVER, value).apply()
    var prefsCar: Long
        get() = prefs().getLong(ID_CAR,0L)
        set(value) = prefs().edit().putLong(ID_CAR, value).apply()
    //Use to save other values
    fun setInt(flag:String,value:Int) = prefs().edit().putInt(flag,value).apply()
    fun getInt(flag: String)= prefs().getInt(flag,0)
    fun setString(flag: String,value:String) = prefs().edit().putString(flag,value).apply()

}