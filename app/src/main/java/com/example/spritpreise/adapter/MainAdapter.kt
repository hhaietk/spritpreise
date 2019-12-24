package com.example.spritpreise.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spritpreise.R
import com.example.spritpreise.model.Station
import kotlinx.android.synthetic.main.view_holder_station.view.*

class MainAdapter(private val mData : MutableList<Station>)
    : RecyclerView.Adapter<MainAdapter.StationViewHolder>() {

    inner class StationViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val brand : TextView = view.vh_brand
        val street : TextView = view.vh_street
        val isOpen : TextView = view.vh_open
        val e5 : TextView = view.vh_e5
        val e10 : TextView = view.vh_e10
        val diesel : TextView = view.vh_diesel
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
        holder.brand.text = mData[position].brand.toLowerCase().capitalize()
        holder.street.text = mData[position].street.toLowerCase().capitalize()

        var colorOpenText = Color.GREEN
        var openText = "Open"

        if (!mData[position].isOpen) {
            colorOpenText = Color.RED
            openText = "Closed"
        }

        holder.isOpen.apply {
            text = openText
            setTextColor(colorOpenText)
        }

        // TODO: Optimize using StringBuilder, not to create new String object each time calling +
        holder.e5.text = "Super E5: ".plus(mData[position].e5).plus("€")
        holder.e10.text = "Super E10: ".plus(mData[position].e10).plus("€")
        holder.diesel.text = "Diesel: ".plus(mData[position].diesel).plus("€")
    }

    fun addData(stations : List<Station>) {
        stations.forEach { station -> mData.add(station) }
        notifyDataSetChanged()
    }

    fun resetData() {
        mData.clear()
    }
}