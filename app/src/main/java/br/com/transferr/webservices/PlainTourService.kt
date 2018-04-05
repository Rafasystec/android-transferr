package br.com.transferr.webservices

import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.Driver
import br.com.transferr.model.PlainTour
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponsePlainTour
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by idoctor on 03/04/2018.
 */
const val BASE_URL = "plaintour"
object PlainTourService: SuperWebService() {
    private var service : IPlainTour = retrofit.create(IPlainTour::class.java)
    fun save(plainTour: PlainTour,responseInterface: OnResponseInterface<PlainTour>){
        service.save(plainTour).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun getDriverPlains(id: Long,responseInterface: OnResponseInterface<List<PlainTour>>){
        service.getByDriver(id).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun save(responsePlainTour: ResponsePlainTour,responseInterface: OnResponseInterface<PlainTour>){
        service.save(responsePlainTour).enqueue(HelperCallBackWebService(responseInterface))
    }

}

interface IPlainTour{
    @POST(BASE_URL)
    fun save(@Body plainTour: PlainTour): Call<PlainTour>
    @POST(BASE_URL)
    fun save(@Body responsePlainTour: ResponsePlainTour): Call<PlainTour>
    @GET(BASE_URL+"/{id}")
    fun get(@Path("id") id:Int): Call<PlainTour>
    @GET(BASE_URL+"/bydriver/{id}")
    fun getByDriver(@Path("id") id:Long): Call<List<PlainTour>>
}