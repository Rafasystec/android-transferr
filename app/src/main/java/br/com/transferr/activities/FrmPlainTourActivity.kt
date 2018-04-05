package br.com.transferr.activities


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.com.transferr.R
import br.com.transferr.adapters.SpTourOptionAdapter
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.showError
import br.com.transferr.extensions.showValidation
import br.com.transferr.extensions.toast
import br.com.transferr.model.PlainTour
import br.com.transferr.model.TourOption
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponsePlainTour
import br.com.transferr.webservices.PlainTourService
import br.com.transferr.webservices.TourOptionService
import kotlinx.android.synthetic.main.activity_frm_plain_tour.*

class FrmPlainTourActivity : AppCompatActivity() {

    var requestPlain=ResponsePlainTour()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frm_plain_tour)
        setupToolbar(R.id.toolbar,"Novo passeio",true)
        btnSave.setOnClickListener {
            callWSToSaveNewPlainTour()
        }
        getAllTourOption()
        initializeSpinnerSeats()
    }

    private fun callWSToSaveNewPlainTour(){
        PlainTourService.save(requestPlain,
                object : OnResponseInterface<PlainTour>{
                    override fun onSuccess(body: PlainTour?) {
                        toast("Saved success!!")
                    }

                    override fun onError(message: String) {
                        showValidation(message)
                    }

                    override fun onFailure(t: Throwable?) {
                        showError(t)
                    }

                }
                )
    }

    fun getAllTourOption(){
        TourOptionService.getAll(
            object : OnResponseInterface<List<TourOption>>{
                override fun onSuccess(body: List<TourOption>?) {
                    initializeSpinnerLocation(body!!)
                }

                override fun onError(message: String) {
                    showValidation(message)
                }

                override fun onFailure(t: Throwable?) {
                    showError(t)
                }
            }
        )
    }
    private fun initializeSpinnerLocation(tourOptions:List<TourOption>){
        spLocation.adapter = SpTourOptionAdapter(this,tourOptions)
        spLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                toast("Selecione uma localidade!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var selectedLocation = tourOptions[position]
                if(selectedLocation != null){
                    requestPlain.idTourOption = selectedLocation.id!!
                }
            }

        }
    }

    private fun initializeSpinnerSeats(){
        var range = arrayListOf(1,2,3,4,5,6,7,8,9,10)
        spSeats.adapter = ArrayAdapter<Int>(this,R.layout.support_simple_spinner_dropdown_item,range)
        spSeats.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                toast("Informe quantidade inicial de passageiros")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var selectedNumber = range[position]
                requestPlain.seatsBusy = selectedNumber
            }

        }
    }
}
