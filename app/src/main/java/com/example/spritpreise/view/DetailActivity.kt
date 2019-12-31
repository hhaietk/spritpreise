package com.example.spritpreise.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spritpreise.R
import com.example.spritpreise.model.Station
import com.example.spritpreise.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mStation : Station? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mStation = intent.getParcelableExtra(Constants.EXTRA_STATION)

        initToolbar()

        //initialize MapView
        map_view_detail.onCreate(savedInstanceState)
        map_view_detail.getMapAsync(this)

        initActions()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val stationLocation = LatLng(mStation!!.lat.toDouble(), mStation!!.lng.toDouble())

        val markerOptions = MarkerOptions()
        markerOptions.apply {
            position(stationLocation)
            title(mStation!!.brand)
        }

        googleMap.apply {
            setMinZoomPreference(13f)
            addMarker(markerOptions)
            moveCamera(CameraUpdateFactory.newLatLng(stationLocation))
            uiSettings.setAllGesturesEnabled(false)
        }
    }

    override fun onResume() {
        map_view_detail.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view_detail.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view_detail.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view_detail.onLowMemory()
    }

    private fun initToolbar() {
        toolbar_detail.apply {
            setNavigationIcon(R.drawable.arrow_back_black_24dp)
            title = mStation?.brand
        }

        setSupportActionBar(toolbar_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar_detail.apply {
            collapsedTitleGravity = Gravity.START
            expandedTitleGravity = Gravity.START or Gravity.BOTTOM
        }
    }

    private fun initActions() {
        floating_btn_direction.setOnClickListener { openGoogleMap() }
    }

    private fun openGoogleMap() {
        val googleMapUri = Uri.parse("geo:0,0?q=${mStation?.lat},${mStation?.lng}(${mStation?.brand})")
        val mapIntent = Intent(Intent.ACTION_VIEW, googleMapUri)
        mapIntent.also {intent ->
            intent.setPackage("com.google.android.apps.maps")
            intent.resolveActivity(packageManager)?.let { startActivity(mapIntent) }
                ?: run { Toast.makeText(
                    this, "Google Maps is not installed", Toast.LENGTH_SHORT).show() }
        }
    }
}