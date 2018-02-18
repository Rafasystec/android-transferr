package br.com.transferr.webservices

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.Car
import br.com.transferr.util.CallRESTMethodsUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by root on 12/02/18.
 */
object CarService : SuperWebService(){
    //private var urlBase = ApplicationTransferr.getInstance().URL_BASE
    /*var callREST = CallRESTMethodsUtil<Car>()
    fun getCar(id:Int): Car {
        var json = callREST.get(urlBase+"car/$id")
        var car = callREST.fromJson<Car>(json)
        //Thread.sleep(2*1000)
        return car
    }*/
    private var service: ICarService
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        service = retrofit.create(ICarService::class.java)
    }

    fun getCar(id:Int): Car {
        var call = service.getCar(id)
        return call.execute().body()!!
    }


}