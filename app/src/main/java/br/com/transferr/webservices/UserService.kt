package br.com.transferr.webservices


import android.content.Context
import br.com.transferr.model.Credentials
import br.com.transferr.model.responses.ResponseLogin
import br.com.transferr.model.responses.ResponseOK
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path

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
        service = retrofit.create(IUserService::class.java)

    }

    fun doLogin(credentials: Credentials):ResponseLogin{
        var response = ResponseLogin()
        if(isConnected()) {
            val callLogin = service.doLogin(credentials)
            response = callLogin.execute().body()!!
        }
        return response!!
    }

    fun changePassword(idUser:Long, actualPassword:String, newPassword:String ):ResponseOK?{
        if(isConnected()){
            return service.changePassword(idUser,actualPassword,newPassword).execute().body()
        }
        return null
    }

    fun recoverPassword(email:String):ResponseOK?{
        if(isConnected()){
            return service.recoverPassword(email).execute().body()
        }
        return null
    }

    fun getService(): IUserService{
        return service
    }
}