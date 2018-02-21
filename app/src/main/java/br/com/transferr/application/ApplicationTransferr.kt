package br.com.transferr.application

import android.app.Application

/**
 * Created by idoctor on 07/02/2018.
 */
class ApplicationTransferr : Application() {
    private val TAG = "APPLICATION"
    private val base = "http://192.168.15.7:8080/"
    val URL_BASE        = base +"transferr-rest/rest/"
    val URL_BASE_IMG    = base +"files/"
    //val URL_BASE = "http://192.168.0.102:8080/transferr-rest/rest/"
    override fun onCreate() {
        super.onCreate()
        //Salva a intancia para termos acesso como Sigleton
        appInstance = this
    }

    companion object {
        //Singleton da classe application
        private var appInstance: ApplicationTransferr? = null
        fun getInstance(): ApplicationTransferr{
            if (appInstance == null){
                throw IllegalStateException("Configure the Application class on Manifest xml.")
            }
            return appInstance!!
        }
    }

}