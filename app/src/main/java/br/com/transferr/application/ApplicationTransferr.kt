package br.com.transferr.application

import android.app.Application

/**
 * Created by idoctor on 07/02/2018.
 */
class ApplicationTransferr : Application() {
    private val TAG = "APPLICATION"

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