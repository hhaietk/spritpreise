package com.example.spritpreise.view

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spritpreise.R
import com.example.spritpreise.view.adapter.MainAdapter
import com.example.spritpreise.view.fragment.SettingsFragment
import com.example.spritpreise.model.Station
import com.example.spritpreise.utils.Constants
import com.example.spritpreise.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_cor.*
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mMainAdapter: MainAdapter
    private lateinit var mLocationManager : LocationManager
    private val mStations : MutableList<Station> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: make home a separate fragment
        setContentView(R.layout.activity_main_cor)

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        initUi()

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mMainViewModel.fetchStations(52.521f, 13.440946f)
        mMainViewModel.stationsLiveData.observe(this, Observer { stations ->
            mMainAdapter.apply {
                resetData()
                addData(stations)
                Timer().schedule(timerTask {
                    runOnUiThread {
                        showRefreshing(false)
                    }
                }, 2000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.cancelAllRequests()
    }

    // Create icons on toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh_btn -> checkLocationPermission()
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
            mMainViewModel.fetchStations(it.latitude.toFloat(), it.longitude.toFloat())
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

            if (item.itemId != R.id.menu_home) showRefreshing(false)

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

        main_refresh_layout.setOnRefreshListener {
            checkLocationPermission()
        }

    }

    private fun showRefreshing(refresh : Boolean) {
        main_refresh_layout.isRefreshing = refresh
        if (refresh) activity_main_layout.visibility = View.GONE else activity_main_layout.visibility = View.VISIBLE
    }

    private fun requestLocation() {
        try {
            mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null)
            showRefreshing(true)
        } catch (exception : SecurityException) {
            Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            showRefreshing(false)
        }
    }

    private fun checkLocationPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
            or (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)) {
            // Permission is not granted, request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.PERMISSION_REQUEST_LOCATION)
        } else {
            // Permission is granted, continue
            requestLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.PERMISSION_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, proceed to request location
                    requestLocation()
                } else {
                    // permission denied
                    Toast.makeText(this, getString(R.string.toast_grant_permission), Toast.LENGTH_LONG).show()
                    showRefreshing(false)
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }
}
