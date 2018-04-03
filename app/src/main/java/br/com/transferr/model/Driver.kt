package br.com.transferr.model

import java.util.*

/**
 * Created by idoctor on 08/02/2018.
 */
class Driver : Entity(){

    var name: String = ""
    var countryRegister: String = ""
    var birthDate: Int = 0
    var user:User?=null
    var group:Grouping?=null
}