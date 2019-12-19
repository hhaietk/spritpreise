package com.example.spritpreise.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spritpreise.R
import com.example.spritpreise.fragment.SettingsFragment
import com.example.spritpreise.viewmodel.StationViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

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

        initUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        stationViewModel.cancelAllRequests()
    }

    // Create icons on toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        for (index in 0 until menu.size()) {
            val drawable = menu.getItem(index).icon

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                drawable.setColorFilter(getColor(R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP)
            } else {
                drawable.colorFilter = BlendModeColorFilter(R.color.md_white_1000, BlendMode.SRC_ATOP)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings_btn -> launchSettingsFragment()
            R.id.menu_location_btn -> Toast.makeText(this, "Location clicked", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    // Press back to quit
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.app_name))
                .setMessage("Do you want to quit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes) { _: DialogInterface, _: Int ->
                    finish()
                    exitProcess(0)
                }
                .show()
        }

        return true
    }

    private fun launchSettingsFragment() {
        recycler_view_main.visibility = View.GONE

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment.newInstance())
            .commit()
    }
    private fun launchIntro() {
        val intent = Intent(this, ActivityIntro::class.java)
        startActivity(intent)
    }

    private fun initUi() {
        recycler_view_main.visibility = View.VISIBLE
        recycler_view_main.setHasFixedSize(true)
        recycler_view_main.layoutManager = LinearLayoutManager(this)

    }
}
