package com.droid2developers.auction.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.droid2developers.auction.R
import com.droid2developers.auction.adapters.SlideShowAdapter
import com.droid2developers.auction.utils.Constants.DELAY_SLIDE
import com.droid2developers.auction.utils.Constants.EXTRA_LOGIN_PROGRESS
import com.droid2developers.auction.utils.Constants.PERIOD_SLIDE
import com.droid2developers.auction.utils.SmartPreferences
import com.google.android.material.card.MaterialCardView
import java.util.*
import kotlin.math.abs

class LoginActivity : AppCompatActivity() {

    private var viewPager: ViewPager2? = null
    private var adapter: SlideShowAdapter? = null
    private var skipButton: RelativeLayout? = null
    private var clockImage: ImageView? = null
    private var antiClockImage: ImageView? = null
    private var phoneLogin: MaterialCardView? = null
    private var preferences: SmartPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewPager = findViewById(R.id.viewPagerId)
        skipButton = findViewById(R.id.skipButtonId)
        phoneLogin = findViewById(R.id.phoneLoginID)
        clockImage = findViewById(R.id.pager_backId)
        antiClockImage = findViewById(R.id.pager_reverseId)
        preferences = SmartPreferences.getInstance(applicationContext)

        val rotate = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 8000
        rotate.fillAfter = true
        rotate.repeatCount = Animation.INFINITE
        rotate.interpolator = LinearInterpolator()


        val reverse = RotateAnimation(
            360f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        reverse.duration = 12000
        reverse.fillAfter = true
        reverse.repeatCount = Animation.INFINITE
        reverse.interpolator = LinearInterpolator()

        clockImage?.startAnimation(rotate)
        antiClockImage?.startAnimation(reverse)
        //imageView.setRotation(180);

        //imageView.setRotation(180);
        adapter = SlideShowAdapter()
        viewPager?.adapter = adapter

        viewPager?.setPageTransformer { page, position ->
            if (position <= -1 || position >= 1) {
                page.findViewById<View>(R.id.imageId).visibility = View.VISIBLE
            } else if (position == 0f) {
                page.findViewById<View>(R.id.imageId).visibility = View.VISIBLE
            } else {
                page.findViewById<View>(R.id.imageId).translationX = -position * page.width / 2
                //Use this to add white shadow while sliding
                page.alpha = 1.0f - abs(position)
            }
        }


        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            when (viewPager?.currentItem) {
                0 -> {
                    viewPager?.setCurrentItem(1, true)
                }
                1 -> {
                    viewPager?.setCurrentItem(2, true)
                }
                else -> {
                    viewPager?.setCurrentItem(0, true)
                }
            }
        }





        Handler(Looper.getMainLooper()).postDelayed({
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(runnable)
                }
            }, DELAY_SLIDE, PERIOD_SLIDE)
        }, 1500)


        skipButton?.setOnClickListener {
            preferences?.saveValue(EXTRA_LOGIN_PROGRESS, 2)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        phoneLogin?.setOnClickListener {
            val intent = Intent(this@LoginActivity, PhoneActivity::class.java)
            startActivity(intent)
        }

    }
}