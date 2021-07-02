package com.droid2developers.auction.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.orhanobut.logger.Logger
import java.util.*

class TabAdapter(manager: FragmentManager?)
        : FragmentStatePagerAdapter(manager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ) {

    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        Logger.d("addFragment: " + fragment.javaClass)
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun clearResources() {
        mFragmentList.clear()
        mFragmentTitleList.clear()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}