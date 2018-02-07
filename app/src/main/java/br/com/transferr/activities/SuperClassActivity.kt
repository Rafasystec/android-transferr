package br.com.transferr.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity

/**
 * Created by root on 06/02/18.
 */

open class SuperClassActivity : AppCompatActivity(){
    //Propriedade para acessar o contexto de qualquer lugar
    protected val context: Context get() = this
    //Métodos comuns para todas as activities
}