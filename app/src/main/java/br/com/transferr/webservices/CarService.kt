package br.com.transferr.webservices

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.Car
import br.com.transferr.util.CallRESTMethodsUtil

/**
 * Created by root on 12/02/18.
 */
class CarService {
    var urlBase = ApplicationTransferr.getInstance().URL_BASE
    var callREST = CallRESTMethodsUtil<Car>()
    public fun getCar(id:Int): Car {
        var car = callREST.get(urlBase+"/car/$id")
        return null!!
    }
}