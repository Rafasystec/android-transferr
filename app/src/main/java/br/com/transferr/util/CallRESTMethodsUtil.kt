package br.com.transferr.util

import android.util.Log
import br.com.transferr.extensions.fromJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

/**
 * Created by idoctor on 08/02/2018.
 */

open class CallRESTMethodsUtil <T>{
    private val TAG = "HTTP_REST"
    private val LOG_ON =true
    val JSON = MediaType.parse("application/json; charset=utf-8")
    var client = OkHttpClient()
    //GET
    fun get(url: String):String{
        val request = Request.Builder().url(url).get().build()
        return getJson(request)
    }

    //POST com JSON
    fun post(url: String,json:String):String{
        log("POST $url --> $json")
        val body = RequestBody.create(JSON,json)
        val request =Request.Builder().url(url).post(body).build()
        return getJson(request)
    }
    //delete
    fun delete(url: String):String{
        val request = Request.Builder().url(url).delete().build()
        return getJson(request)
    }
    //le resposta do servidor em formato JSON
    private fun getJson(request:Request?): String{
        val response = client.newCall(request).execute()
        val responseBody = response.body()
        if(responseBody != null){
            val json = responseBody.string()
            log("JSON returned -->> $json")
            //return fromJson<T>(json)
            return json
        }
        throw IOException("Erro ao fazer o requisição")
    }

    private fun log(event:String){
        if(LOG_ON){
            Log.d(TAG,event)
        }
    }

    inline fun <reified T> fromJson(json: String): T{
        val type = object : TypeToken<T>(){}.type
        return Gson().fromJson<T>(json,type)
    }
}