package com.example.spritpreise.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spritpreise.R
import com.example.spritpreise.adapter.MainAdapter
import com.example.spritpreise.fragment.SettingsFragment
import com.example.spritpreise.model.Station
import com.example.spritpreise.utils.Constants
import com.example.spritpreise.viewmodel.StationViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_cor.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var mStationViewModel: StationViewModel
    private lateinit var mMainAdapter: MainAdapter
    private lateinit var locationManager : LocationManager
    private val mStations : MutableList<Station> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: make home a separate fragment
        setContentView(R.layout.activity_main_cor)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        initUi()

        mStationViewModel = ViewModelProviders.of(this).get(StationViewModel::class.java)
        mStationViewModel.fetchStations(52.521f, 13.440946f)
        mStationViewModel.stationsLiveData.observe(this, Observer { stations ->
            mMainAdapter.apply {
                resetData()
                addData(stations)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mStationViewModel.cancelAllRequests()
    }

    // Create icons on toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh_btn -> {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0L, 0f, this)
                } catch (exception : SecurityException) {
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Press back to quit
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.app_name))
                .setMessage(getString(R.string.quit_confirmation))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes) { _: DialogInterface, _: Int ->
                    finish()
                    exitProcess(0)
                }
                .show()
        }

        return true
    }

    override fun onLocationChanged(location: Location?) {
        location?.let {it ->
            mStationViewModel.fetchStations(it.latitude.toFloat(), it.longitude.toFloat())
        } ?: run { Toast.makeText(this, "Location is not available", Toast.LENGTH_SHORT).show() }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    private fun launchSettingsFragment() {
        recycler_view_main.visibility = View.GONE
        fragment_container.visibility = View.VISIBLE

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun initUi() {

        mMainAdapter = MainAdapter(mStations)
        val layoutManagerLinear = LinearLayoutManager(this)

        recycler_view_main.apply {
            visibility = View.VISIBLE
            setHasFixedSize(true)
            layoutManager = layoutManagerLinear
            adapter = mMainAdapter
        }

        mMainAdapter.onItemClick = { station ->
            val i = Intent(this, DetailActivity::class.java)
            i.putExtra(Constants.EXTRA_STATION, station)
            startActivity(i)
        }

        bottom_bar_main_act.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_settings -> launchSettingsFragment()
                R.id.menu_home -> {
                    recycler_view_main.visibility = View.VISIBLE
                    fragment_container.visibility = View.GONE
                }
                // TODO: implement other fragments
            }

            true
        }

    }
}
