package br.com.transferr.webservices

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.Car
import br.com.transferr.model.Driver
import br.com.transferr.util.CallRESTMethodsUtil

/**
 * Created by root on 12/02/18.
 */
class DriverService {
    private var urlBase = ApplicationTransferr.getInstance().URL_BASE
    var callREST = CallRESTMethodsUtil<Driver>()
    fun getDriver(id:Int): Driver {
        var json = callREST.get(urlBase+"driver/$id")
        var driver = callREST.fromJson<Driver>(json)
        Thread.sleep(5*1000)
        return driver
    }
}