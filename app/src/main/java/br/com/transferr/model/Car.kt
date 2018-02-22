package br.com.transferr.model

import android.os.Parcel
import br.com.transferr.model.enums.EnumStatus

/**
 * Created by idoctor on 08/02/2018.
 */
class Car : Entity(){


    var model: String? = ""
    var carIdentity: String?=null
    var color:String?=null
    var externalEquip:Boolean = false
    var driver:Driver?= null
    var status:EnumStatus=EnumStatus.OFFLINE

}


