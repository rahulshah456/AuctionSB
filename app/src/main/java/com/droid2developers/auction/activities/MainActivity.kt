package com.droid2developers.auction.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.droid2developers.auction.R
import com.droid2developers.auction.fragments.AccountFragment
import com.droid2developers.auction.fragments.AuctionsFragment
import com.droid2developers.auction.fragments.BiddingFragment
import com.droid2developers.auction.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationId)
        bottomNavigationView?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.homeId -> {
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.auctionsId -> {
                replaceFragment(AuctionsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.biddingsId -> {
                replaceFragment(BiddingFragment.newInstance("", true))
                return@OnNavigationItemSelectedListener true
            }
            R.id.accountId -> {
                replaceFragment(AccountFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameId, fragment, fragment.javaClass.simpleName)
            .commit()
    }
}