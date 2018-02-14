package br.com.transferr.webservices

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.Car
import br.com.transferr.util.CallRESTMethodsUtil

/**
 * Created by root on 12/02/18.
 */
class CarService {
    private var urlBase = ApplicationTransferr.getInstance().URL_BASE
    var callREST = CallRESTMethodsUtil<Car>()
    fun getCar(id:Int): Car {
        var json = callREST.get(urlBase+"car/$id")
        var car = callREST.fromJson<Car>(json)
        Thread.sleep(2*1000)
        return car
    }
}