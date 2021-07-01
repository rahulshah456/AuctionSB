package com.droid2developers.auction.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.droid2developers.auction.R
import com.droid2developers.auction.utils.Constants.BIDDER_ACCOUNT
import com.droid2developers.auction.utils.Constants.EXTRA_ACCOUNT_TYPE
import com.droid2developers.auction.utils.Constants.EXTRA_PHONE_NUMBER
import com.droid2developers.auction.utils.Constants.EXTRA_LOGIN_PROGRESS
import com.droid2developers.auction.utils.Constants.SKIPPED_ACCOUNT
import com.droid2developers.auction.utils.SmartPreferences
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.logger.Logger

class SplashActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var preferences: SmartPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //init firebase
        FirebaseApp.initializeApp(applicationContext)

        //init arguments
        preferences = SmartPreferences.getInstance(applicationContext)
        mAuth = FirebaseAuth.getInstance()

        //session
        loginSession()
        finish()
    }

    private fun loginSession() {

        val loginState = preferences?.getValue(EXTRA_LOGIN_PROGRESS, -1)
        if (loginState == -1) {
            //default
            Logger.d("loginSession: default")
            launchActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            return
        }

        when (loginState) {
            0 -> {
                //incomplete login
                Logger.d("loginSession: Incomplete")
                val intent = Intent(this@SplashActivity, CompleteActivity::class.java)
                intent.putExtra(EXTRA_PHONE_NUMBER, mAuth?.currentUser?.phoneNumber)
                launchActivity(intent)
            }
            1 -> {
                //logged in
                if (mAuth?.currentUser == null) {
                    Logger.d("loginSession: authentication expired")
                    launchActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    return
                }

                val accountType = preferences?.getValue(EXTRA_ACCOUNT_TYPE, BIDDER_ACCOUNT)
                Logger.d("loginSession:$accountType")
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra(EXTRA_ACCOUNT_TYPE, accountType)
                launchActivity(intent)
            }
            2 -> {
                // Skipped logged user
                Logger.d("loginSession: Skipped")
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra(EXTRA_ACCOUNT_TYPE, SKIPPED_ACCOUNT)
                launchActivity(intent)
            }
        }
    }

    private fun launchActivity(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}