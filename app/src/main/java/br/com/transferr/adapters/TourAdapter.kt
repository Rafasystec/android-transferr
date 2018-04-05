package br.com.transferr.adapters

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.transferr.R
import br.com.transferr.model.PlainTour
import br.com.transferr.util.AddPassengerDialog
import kotlinx.android.synthetic.main.rc_view_tours.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

/**
 * Created by root on 02/04/18.
 */
class TourAdapter(private val tours:List<PlainTour>,private val context: Context, val activity: Activity) : Adapter<TourAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rc_view_tours, parent, false)
        return ViewHolder(view,context)
    }

    override fun getItemCount(): Int {
       return tours.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let {
            var plainTour = tours[position]
            it.bindView(plainTour)
            it.itemView.setOnClickListener {
                Toast.makeText(context,"Ao clicar será modificado",Toast.LENGTH_SHORT).show()
                val dialog = AddPassengerDialog(context,activity,plainTour)
                dialog.create()
            }
        }

    }

    class ViewHolder(itemView:View,val context: Context) : RecyclerView.ViewHolder(itemView){
        fun bindView(tour: PlainTour) {
            val titleView       = itemView.note_item_title
            val description = itemView.note_item_description
            val seats       = itemView.seats
            titleView.text          = tour.tourOption?.name
            description.text    = tour.tourOption?.description
            seats.text          = "Vagas: " + tour.seatsRemaining
            itemView.imgTrash.setOnClickListener {
                //Toast.makeText(context,"Excluir item ${tour.id}",Toast.LENGTH_LONG).show()
                context.alert("Confirme exclusão"){
                    title = "Exclusão"
                    yesButton { excluir(tour) }
                    noButton {  }
                }.show()
            }
        }

        private fun excluir(tour: PlainTour){
            Toast.makeText(context,"Excluir item ${tour.id}",Toast.LENGTH_LONG).show()
        }
    }
}


