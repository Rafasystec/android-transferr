package br.com.transferr.webservices


import br.com.transferr.model.Credentials
import br.com.transferr.model.responses.ResponseLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by root on 20/02/18.
 */
interface IUserService {
    @POST("user/login")
    fun doLogin(@Body credentials: Credentials): Call<ResponseLogin>
}