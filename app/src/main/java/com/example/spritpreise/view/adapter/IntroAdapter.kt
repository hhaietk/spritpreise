package com.example.spritpreise.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class IntroAdapter(introPages: IntArray, context: Context) : PagerAdapter() {

    private lateinit var mLayoutInflater: LayoutInflater
    private var mIntroPages : IntArray = introPages
    private var mContext : Context = context

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