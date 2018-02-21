package br.com.transferr.webservices


import br.com.transferr.model.AnexoPhoto
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.ResponseOK
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by root on 21/02/18.
 */
interface IDriverService  {
    @POST("driver/profile/photo")
    fun savePhoto(@Body anexoPhoto: AnexoPhoto):Call<ResponseOK>
    @GET("driver/{id}")
    fun getDriver(@Path("id") id:Int): Call<Driver>
}