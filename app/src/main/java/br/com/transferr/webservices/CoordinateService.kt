package br.com.transferr.webservices

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.extensions.fromJson
import br.com.transferr.extensions.toJson
import br.com.transferr.model.Car
import br.com.transferr.model.Coordinates
import br.com.transferr.model.Response
import br.com.transferr.util.CallRESTMethodsUtil

/**
 * Created by idoctor on 08/02/2018.
 */

object CoordinateService{
    //private val BASE_URL = "http://192.168.15.7:8080/transferr-rest/rest/"
    private var urlBase = ApplicationTransferr.getInstance().URL_BASE
    var callREST = CallRESTMethodsUtil<Coordinates>()
    //Salvar a coordenada
    //TODO Implements later
    fun save(coordinates: Coordinates): Coordinates{
        coordinates.id = 1
        val json = callREST.post(urlBase+"coordinatescar",coordinates.toJson())
        return callREST.fromJson(json)
        //return null!!
    }


}