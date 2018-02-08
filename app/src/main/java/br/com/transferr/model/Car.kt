package br.com.transferr.model

import br.com.transferr.model.enums.EnumStatus

/**
 * Created by idoctor on 08/02/2018.
 */
data class Car(var model: String,
               var carIdentity: String,
               var color:String,
                var externalEquip:Boolean,
               var dirver:Driver,
               var status:EnumStatus) : Entity(){

    //private val model: String? = null
    //private val carIdentity: String? = null
    //private val color: String? = null
    //private val externalEquip: Boolean? = null
    //private val driver: Driver? = null
    //private val status: EnumStatus? = null

}