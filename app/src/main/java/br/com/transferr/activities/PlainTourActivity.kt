package br.com.transferr.activities

import android.app.AlertDialog
import android.content.Intent
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
import kotlinx.android.synthetic.main.rc_view_tours.*

class PlainTourActivity : SuperClassActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plain_tour)
        setupToolbar(R.id.toolbar,"Passeios",true)
        createListTour(emptyList())
        callWSToGetAllOpenDriverPlainTour()
        fabAddPlainTour.setOnClickListener {
           startFrmPlainTourActivity()
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
        recyclerView.adapter        = TourAdapter(plainTours, context,this)
        val layoutManager           = GridLayoutManager(this, GridLayoutManager.VERTICAL)
        recyclerView.layoutManager  = layoutManager
    }

    private fun callWSToGetAllOpenDriverPlainTour(){
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

    private fun startFrmPlainTourActivity(){
        startActivity(Intent(context,FrmPlainTourActivity::class.java))
    }

    private fun alertConfirm(){

    }

}
