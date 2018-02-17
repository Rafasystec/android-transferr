package br.com.transferr.webservices

import br.com.transferr.model.Quadrant
import br.com.transferr.model.responses.ResponsePassengersOnline
import br.com.transferr.util.CallRESTMethodsUtil

/**
 * Created by root on 16/02/18.
 */
class CoordinatePassService : SuperWebService(){
    var callRESTOnline = CallRESTMethodsUtil<MutableList<ResponsePassengersOnline>>()
    fun getOnlinePassengers(quadrant: Quadrant):List<ResponsePassengersOnline>{
        var stringBuilder = StringBuilder("pass/coordinates/online/")
        //Far points location
        stringBuilder.append(quadrant.farLeftLng).append("/")
        stringBuilder.append(quadrant.farLeftLat).append("/")
        stringBuilder.append(quadrant.farRightLng).append("/")
        stringBuilder.append(quadrant.farRightLat).append("/")
        //Near points location
        stringBuilder.append(quadrant.nearLeftLng).append("/")
        stringBuilder.append(quadrant.nearLeftLat).append("/")
        stringBuilder.append(quadrant.nearLeftLng).append("/")
        stringBuilder.append(quadrant.nearRightLat).append("")
        var url = urlBase+stringBuilder.toString()

        //TODO comment for a while
        var json = callRESTOnline.get(url)
        return callRESTOnline.fromJson<MutableList<ResponsePassengersOnline>>(json)
    }
}