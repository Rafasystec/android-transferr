package br.com.transferr.webservices

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.Car
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseLogin
import br.com.transferr.util.CallRESTMethodsUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by root on 12/02/18.
 */
object CarService : SuperWebService(){
    private var service: ICarService
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        service = retrofit.create(ICarService::class.java)
    }

    fun getCar(id:Long,responseInterface: OnResponseInterface<Car>) {
        service.getCar(id).enqueue(HelperCallBackWebService(responseInterface))

    }

    fun getCarByUser(id:Long,responseInterface: OnResponseInterface<Car>) {
        service.getCarByUser(id).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun getService(): ICarService{
        return service
    }


}