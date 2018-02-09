package br.com.transferr.webservices

import br.com.transferr.extensions.fromJson
import br.com.transferr.extensions.toJson
import br.com.transferr.model.Coordinates
import br.com.transferr.model.Response
import br.com.transferr.util.CallRESTMethodsUtil

/**
 * Created by idoctor on 08/02/2018.
 */

object CoordinateService{
    private val BASE_URL = "http://192.168.0.102:8080/transferr-rest/rest/"
    //Salvar a coordenada
    fun save(coordinates: Coordinates): Response{
        val json = CallRESTMethodsUtil.post(BASE_URL+"coordinates",coordinates.toJson())
        val response = fromJson<Response>(json)
        return response
    }
}