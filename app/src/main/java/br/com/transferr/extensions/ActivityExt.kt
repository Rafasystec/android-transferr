package br.com.transferr.extensions

import android.app.Activity
import android.support.annotation.IdRes
import android.widget.TextView
import android.widget.Toast

/**
 * Created by root on 06/02/18.
 */

fun Activity.toast(message: CharSequence, lenth: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this,message,lenth).show()
fun Activity.getTextAsString(@IdRes id:Int):String{
    val textView = findViewById<TextView>(id)
    return textView.text.toString()
}
