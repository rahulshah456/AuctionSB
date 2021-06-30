package com.droid2developers.auction.utils;

import android.content.Context;
import android.content.SharedPreferences;
import static com.droid2developers.auction.utils.Constants.PREF_NAME;

public class SmartPreferences {

    private static volatile SmartPreferences instance;
    private final SharedPreferences sharedPreferences;

    public static SmartPreferences getInstance(Context context) {
        if (instance == null) {
            synchronized (SmartPreferences.class) {
                if (instance == null) {
                    instance = new SmartPreferences(context);
                }
            }
        }
        return instance;
    }

    private SmartPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    //Overloaded setter functions for key value pairs calls
    public void saveValue(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public void saveValue(String key, Long value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putLong(key, value);
        prefsEditor.apply();
    }

    public void saveValue(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }

    public void saveValue(String key, Boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public void saveValue(String key, Float value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putFloat(key, value);
        prefsEditor.apply();
    }


    //Overloaded getter functions for key default value pairs
    public String getValue(String key, String defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public Long getValue(String key, Long defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    public Integer getValue(String key, Integer defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public Boolean getValue(String key, Boolean defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    public Float getValue(String key, Float defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getFloat(key, defaultValue);
        }
        return defaultValue;
    }


}
