package br.com.transferr.model

import java.util.*

/**
 * Created by idoctor on 08/02/2018.
 */
data class Driver (var name: String,
                   var countryRegister: String,
                   var birthDate: Date): Entity(){

   // private val name: String? = null
   // private val countryRegister: String? = null
   // private val birthDate: Date? = null
}