package com.example.spritpreise.utils

import android.app.Activity
import android.view.WindowManager

object UiTools {

    fun setStatusBarColor(act : Activity, color : Int) {
        val window = act.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }
}