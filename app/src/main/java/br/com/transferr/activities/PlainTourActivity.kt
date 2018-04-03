package br.com.transferr.activities

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import br.com.transferr.R
import br.com.transferr.adapters.TourAdapter
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.showError
import br.com.transferr.extensions.showValidation
import br.com.transferr.extensions.toast
import br.com.transferr.fragments.DialogAddPlainFragment
import br.com.transferr.model.PlainTour
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.util.Prefes
import br.com.transferr.webservices.PlainTourService
import kotlinx.android.synthetic.main.activity_plain_tour.*
import kotlinx.android.synthetic.main.dialog_add_plaintour.view.*

class PlainTourActivity : SuperClassActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plain_tour)
        setupToolbar(R.id.toolbar,"Passeios",true)
        createListTour(emptyList())
        callWSToGetAllOpenDirverPlainTour()

        fabAddPlainTour.setOnClickListener {
            showEditDialog()
            /*
            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_plaintour,null)
            dialog.setView(dialogView)
            dialog.setCancelable(true)
            dialog.show()

            dialogView.btnSavePlain.setOnClickListener {
                toast(" Did you press OK")
            }
            dialogView.btnCancelPlain.setOnClickListener {
            }
            */
        }

    }

    private fun initProgressBar(){
        this@PlainTourActivity.runOnUiThread({
            progressBar.visibility = View.VISIBLE
        })
    }

    private fun stopProgressBar(){
        this@PlainTourActivity.runOnUiThread({
            progressBar.visibility = View.GONE
        })
    }

    private fun createListTour(plainTours:List<PlainTour>){
        val recyclerView            = rcviewTours
        recyclerView.adapter        = TourAdapter(plainTours, context)
        val layoutManager           = GridLayoutManager(this, GridLayoutManager.VERTICAL)
        recyclerView.layoutManager  = layoutManager
    }

    private fun callWSToGetAllOpenDirverPlainTour(){
        initProgressBar()
        PlainTourService.getDriverPlains(Prefes.prefsDriver,
                object : OnResponseInterface<List<PlainTour>> {
                    override fun onSuccess(body: List<PlainTour>?) {
                        stopProgressBar()
                        createListTour(body!!)
                    }

                    override fun onError(message: String) {
                        stopProgressBar()
                        createListTour(emptyList())
                        showValidation(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        stopProgressBar()
                        createListTour(emptyList())
                        showError(t?.message!!)
                    }
         })
    }

    private fun showEditDialog() {
        val fm = supportFragmentManager
        val editNameDialogFragment = DialogAddPlainFragment()
        editNameDialogFragment.show(fm, "fragment_edit_name")
    }

}
