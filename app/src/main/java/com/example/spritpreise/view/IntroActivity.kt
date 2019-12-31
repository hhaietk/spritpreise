package com.example.spritpreise.view

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import com.example.spritpreise.R
import com.example.spritpreise.view.adapter.IntroAdapter
import com.example.spritpreise.utils.UiTools
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    private lateinit var mIntroPages : IntArray
    private lateinit var mDots : Array<TextView?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        initUi()
        initActions()
    }

    private fun initUi() {
        mIntroPages = intArrayOf(R.layout.intro_first_page, R.layout.intro_second_page)
        setPage(0)
    }

    private fun setPage(currentPage : Int) {
        mDots = arrayOfNulls(mIntroPages.size)

        layout_dots_intro.removeAllViews()

        // populate the grey dots
        for (i in mDots.indices) {
            mDots[i] = TextView(this)
            mDots[i]?.text = Html.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY)
            mDots[i]?.setTextColor(getColor(R.color.md_grey_300))
            mDots[i]?.textSize = 35f
            layout_dots_intro.addView(mDots[i])
        }

        // make the current dots yellow
        if (mDots.isNotEmpty()) {
            mDots[currentPage]?.setTextColor(getColor(R.color.md_yellow_300))
        }
    }

    private fun initActions() {
        val viewPagerPageChangedListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                when(position) {
                    0 -> UiTools.setStatusBarColor(this@IntroActivity, getColor(R.color.md_green_500))
                    1 -> UiTools.setStatusBarColor(this@IntroActivity, getColor(R.color.md_blue_600))
                }
            }

            override fun onPageSelected(position: Int) {
                setPage(position)

                // reached the last page
                if (position == mIntroPages.size - 1) {
                    btn_next_intro.text = "LET'S GO"
                    btn_skip_intro.visibility = View.GONE
                } else {
                    btn_next_intro.text = "Next"
                    btn_skip_intro.visibility = View.VISIBLE
                }
            }
        }

        val introAdapter = IntroAdapter(mIntroPages, this)
        view_pager_intro.adapter = introAdapter
        view_pager_intro.addOnPageChangeListener(viewPagerPageChangedListener)

        btn_skip_intro.setOnClickListener {
            startMainActivity()
        }

        btn_next_intro.setOnClickListener {
            if (view_pager_intro.currentItem < mIntroPages.size - 1) {
                view_pager_intro.currentItem++
            } else {
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}