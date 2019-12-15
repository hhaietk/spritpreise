package com.example.spritpreise.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class IntroAdapter : PagerAdapter {

    private lateinit var mLayoutInflater: LayoutInflater
    private var mIntroPages : IntArray
    private var mContext : Context


    constructor(introPages : IntArray , context : Context) {
        mIntroPages = introPages
        mContext = context
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = mLayoutInflater.inflate(mIntroPages[position], container, false)
        container.addView(view)

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return mIntroPages.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}