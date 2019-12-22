package com.example.spritpreise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spritpreise.R
import com.example.spritpreise.model.Station
import kotlinx.android.synthetic.main.view_holder_station.view.*

class MainAdapter(private val mData : List<Station>)
    : RecyclerView.Adapter<MainAdapter.StationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_station, parent, false)

        return StationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.name.text = mData[position].name
        holder.brand.text = mData[position].brand
        holder.street.text = mData[position].street
        holder.isOpen.text = mData[position].isOpen.toString()
    }

    inner class StationViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.vh_name
        val brand : TextView = view.vh_brand
        val street : TextView = view.vh_street
        val isOpen : TextView = view.vh_open
    }
}