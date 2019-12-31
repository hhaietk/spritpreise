package com.example.spritpreise.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.spritpreise.R
import com.example.spritpreise.model.Station
import com.example.spritpreise.model.StationDetail
import com.example.spritpreise.utils.Constants
import com.example.spritpreise.utils.GeneralTools
import com.example.spritpreise.viewmodel.DetailViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.view.*

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mDetailViewModel: DetailViewModel
    private var mStation : Station? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mStation = intent.getParcelableExtra(Constants.EXTRA_STATION)

        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        mDetailViewModel.fetchStationDetail(mStation!!.id)
        mDetailViewModel.stationDetailLiveData.observe(this, Observer {
                station -> displayAddress(station)
        })

        initToolbar()

        //initialize MapView
        map_view_detail.onCreate(savedInstanceState)
        map_view_detail.getMapAsync(this)

        initUi()
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
            title(mStation!!.brand.toLowerCase().capitalize())
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
        mDetailViewModel.cancelAllRequests()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view_detail.onLowMemory()
    }

    private fun initToolbar() {
        toolbar_detail.apply {
            setNavigationIcon(R.drawable.arrow_back_black_24dp)
            title = mStation!!.brand.toLowerCase().capitalize()
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

    private fun initUi() {
        e5_detail_tv.text = getString(R.string.superE5).plus(" ${mStation?.e5}")
        e10_detail_tv.text = getString(R.string.superE10).plus(" ${mStation?.e10}")
        diesel_detail_tv.text = getString(R.string.diesel).plus(" ${mStation?.diesel}")
        address_desc_tv.text = GeneralTools.formatStreetName(mStation!!.street)
            .plus(" ${mStation?.houseNumber}, ${mStation?.postCode} ${mStation!!.place.toLowerCase().capitalize()}")
    }

    private fun displayAddress(station: StationDetail) {

        val openTime = station.openingTimes

        when (openTime.size) {
            0 -> {
                mo_fr_detail_tv.text = getString(R.string.no_address)
                sa_detail_tv.visibility = View.GONE
                su_detail_tv.visibility = View.GONE
                separator_view.visibility = View.VISIBLE
            }
            1 -> {
                mo_fr_detail_tv.text = getString(R.string.address_detail,
                    openTime[0].day, openTime[0].start, openTime[0].end)
                sa_detail_tv.visibility = View.GONE
                su_detail_tv.visibility = View.GONE
                separator_view.visibility = View.VISIBLE
            }
            2 -> {
                mo_fr_detail_tv.text = getString(R.string.address_detail,
                    openTime[0].day, openTime[0].start, openTime[0].end)
                sa_detail_tv.text = getString(R.string.address_detail,
                    openTime[1].day, openTime[1].start, openTime[1].end)
                su_detail_tv.visibility = View.GONE
                separator_view.visibility = View.VISIBLE
            }
            3 -> {
                mo_fr_detail_tv.text = getString(R.string.address_detail,
                    openTime[0].day, openTime[0].start, openTime[0].end)
                sa_detail_tv.text = getString(R.string.address_detail,
                    openTime[1].day, openTime[1].start, openTime[1].end)
                su_detail_tv.text = getString(R.string.address_detail,
                    openTime[2].day, openTime[2].start, openTime[2].end)
                separator_view.visibility = View.GONE
            }
        }
    }
}