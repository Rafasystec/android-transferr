package br.com.transferr.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import br.com.transferr.R
import br.com.transferr.extensions.toast
import br.com.transferr.model.PlainTour
import kotlinx.android.synthetic.main.dialog_add_plaintour.view.*

/**
 * Created by root on 04/04/18.
 */
class AddPassengerDialog(val context: Context, val activity: Activity,val plainTour: PlainTour) {

    fun create(){
        val dialog = AlertDialog.Builder(context)
        val dialogView = activity.layoutInflater.inflate(R.layout.dialog_add_plaintour,null)
        var totalPassenger = 0
        dialogView.btnSavePlain.setOnClickListener {
            activity.toast("OK pressed ${plainTour.id}")

        }
        dialogView.btnCancelPlain.setOnClickListener {
            activity.toast("Cancel")
        }

        dialogView.btnAddPassenger.setOnClickListener {
            totalPassenger++
            dialogView.lblAddPassenger.text = totalPassenger.toString()
        }

        dialogView.btnLessPassenger.setOnClickListener {
            totalPassenger--
            dialogView.lblAddPassenger.text = totalPassenger.toString()
        }
        dialog.setCancelable(true)
        dialog.setView(dialogView)
        dialog.show()
    }
}