package br.com.transferr.webservices

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.AnexoPhoto
import br.com.transferr.model.Car
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.util.CallRESTMethodsUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

/**
 * Created by root on 12/02/18.
 */
object DriverService :SuperWebService(){
    private var service: IDriverService = retrofit.create(IDriverService::class.java)

    fun getDriver(id:Int): Driver {
        return service.getDriver(id).execute().body()!!
    }

    fun savePhoto(anexoPhoto: AnexoPhoto): ResponseOK?{
        if(isConnected()) {
            return service.savePhoto(anexoPhoto).execute().body()
        }
        return null
    }
}