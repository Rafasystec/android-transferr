package br.com.transferr.webservices

import br.com.transferr.model.Car
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by root on 17/02/18.
 */
interface ICarService {
    @GET("car/{id}")
    fun getCar(@Path("id") id:Int):Call<Car>
}