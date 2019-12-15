package com.example.spritpreise.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.spritpreise.R
import com.example.spritpreise.viewmodel.StationViewModel

class ActivityMain : AppCompatActivity() {

    private lateinit var stationViewModel: StationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchIntro()

        stationViewModel = ViewModelProviders.of(this).get(StationViewModel::class.java)

        stationViewModel.fetchStations()

        stationViewModel.stationsLiveData.observe(this, Observer {

            if (it != null) {
                for (station in it) {
                    Log.d("TEST", "List of stations: $station")
                }
            } else {
                Log.e("TEST", "null")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stationViewModel.cancelAllRequests()
    }

    private fun launchIntro() {
        val intent = Intent(this, ActivityIntro::class.java)
        startActivity(intent)
    }
}
