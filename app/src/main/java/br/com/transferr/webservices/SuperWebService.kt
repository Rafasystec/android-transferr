package br.com.transferr.webservices

import android.content.Context
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.util.NetworkUtil
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by root on 16/02/18.
 */
open class SuperWebService {
    protected var urlBase = ApplicationTransferr.getInstance().URL_BASE
    protected var httpClient:OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10,TimeUnit.SECONDS)
            .writeTimeout(5,TimeUnit.SECONDS)
            .readTimeout(5,TimeUnit.SECONDS)
            .build()

    protected fun isConnected():Boolean{
        return NetworkUtil.isNetworkAvailable(ApplicationTransferr.getInstance().applicationContext)
    }
}