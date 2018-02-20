package br.com.transferr.webservices


import android.content.Context
import br.com.transferr.model.Credentials
import br.com.transferr.model.responses.ResponseLogin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by root on 20/02/18.
 */
object UserService :SuperWebService(){

    private var service: IUserService
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(UserService.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(UserService.httpClient)
                .build()
        UserService.service = retrofit.create(IUserService::class.java)
    }

    fun doLogin(credentials: Credentials):ResponseLogin{
        var response = ResponseLogin()
        if(isConnected()) {
            val callLogin = service.doLogin(credentials)
            response = callLogin.execute().body()!!
        }
        return response!!
    }
}