package com.example.spritpreise.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spritpreise.R
import com.example.spritpreise.model.Station
import com.example.spritpreise.utils.GeneralTools
import kotlinx.android.synthetic.main.view_holder_station.view.*

class MainAdapter(private val mData : MutableList<Station>)
    : RecyclerView.Adapter<MainAdapter.StationViewHolder>() {

    var onItemClick : ((Station) -> Unit)? = null

    inner class StationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { onItemClick?.invoke(mData[adapterPosition]) }
        }

        val brand : TextView = itemView.vh_brand
        val street : TextView = itemView.vh_street
        val isOpen : TextView = itemView.vh_open
        val e5 : TextView = itemView.vh_e5
        val e10 : TextView = itemView.vh_e10
        val diesel : TextView = itemView.vh_diesel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_station, parent, false)

        return StationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = mData[position]

        holder.apply {
            brand.text = station.brand.toLowerCase().capitalize()
            street.text = GeneralTools.formatStreetName(station.street).plus(" ${station.houseNumber}")
        }

        var colorOpenText = Color.GREEN
        var openText = "Open"

        if (!station.isOpen) {
            colorOpenText = Color.RED
            openText = "Closed"
        }

        holder.isOpen.apply {
            text = openText
            setTextColor(colorOpenText)
        }

        holder.apply {
            e5.text = "Super E5: ".plus(station.e5)
            e10.text = "Super E10: ".plus(station.e10)
            diesel.text = "Diesel: ".plus(station.diesel)
        }
    }

    fun addData(stations : List<Station>) {
        stations.forEach { station -> mData.add(station) }
        notifyDataSetChanged()
    }

    fun resetData() {
        mData.clear()
    }
}