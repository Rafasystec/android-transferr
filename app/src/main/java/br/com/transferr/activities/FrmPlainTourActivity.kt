package br.com.transferr.activities


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.com.transferr.R
import br.com.transferr.adapters.SpLocationAdapter
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.toast
import br.com.transferr.model.Location
import br.com.transferr.model.responses.ResponsePlainTour
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
        initializeSpinnerLocation()
        initializeSpinnerSeats()
    }

    private fun callWSToSaveNewPlainTour(){
        toast("Save success!!"+requestPlain.idLocation)

    }
    private fun initializeSpinnerLocation(){
        var location = Location()
        location.id = 1
        location.name = "Duna do por-do-sol"
        location.photoProfile = ""
        var locations = arrayListOf(location)
        spLocation.adapter = SpLocationAdapter(this,locations)//ArrayAdapter<Location>(this,R.layout.support_simple_spinner_dropdown_item,locations)
        spLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                toast("Selecione uma localidade!")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var selectedLocation = locations[position]
                if(selectedLocation != null){
                    requestPlain.idLocation = selectedLocation.id!!
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
