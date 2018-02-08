package br.com.transferr.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by idoctor on 08/02/2018.
 */
object InternetUtil {

    fun isNetworkAvailable(context:Context):Boolean{
        val connenctivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = connenctivity.allNetworks
        for (n in networks){
            val info = connenctivity.getNetworkInfo(n)
            if(info.state == NetworkInfo.State.CONNECTED){
                return true
            }
        }
        return false
    }
}