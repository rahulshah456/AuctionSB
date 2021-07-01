package com.droid2developers.auction.utils

import android.content.Context
import android.content.SharedPreferences
import com.droid2developers.auction.utils.Constants.PREF_NAME

class SmartPreferences private constructor(context: Context) {

    companion object : SingletonHolder<SmartPreferences, Context>(::SmartPreferences)

    private val sharedPreferences: SharedPreferences? =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


    //Overloaded setter functions for key value pairs calls
    fun saveValue(key: String?, value: String?)  = value?.let {
        sharedPreferences?.edit()?.putString(key, value)?.apply()
    }

//    fun saveValue(key: String?, value: Long?) =  value?.let {
//        sharedPreferences?.edit()?.putLong(key, value)?.apply()
//    }

    fun saveValue(key: String?, value: Int?) = value?.let {
        sharedPreferences?.edit()?.putInt(key, value)?.apply()
    }

    fun saveValue(key: String?, value: Boolean?) = value?.let {
        sharedPreferences?.edit()?.putBoolean(key, value)?.apply()
    }

    fun saveValue(key: String?, value: Float?) = value?.let {
        sharedPreferences?.edit()?.putFloat(key, value)?.apply()
    }


    //Overloaded getter functions for key default value pairs
    fun getValue(key: String?, defaultValue: String?) =
        sharedPreferences?.getString(key, defaultValue) ?: defaultValue

    fun getValue(key: String?, defaultValue: Long) =
        sharedPreferences?.getLong(key, defaultValue) ?: defaultValue

    fun getValue(key: String?, defaultValue: Int) =
        sharedPreferences?.getInt(key, defaultValue) ?: defaultValue

    fun getValue(key: String?, defaultValue: Boolean) =
        sharedPreferences?.getBoolean(key, defaultValue) ?: defaultValue

    fun getValue(key: String?, defaultValue: Float) =
        sharedPreferences?.getFloat(key, defaultValue) ?: defaultValue
}