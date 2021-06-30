package com.droid2developers.auction

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


class AuctionApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //custom logger init
        Logger.addLogAdapter(AndroidLogAdapter())
    }

}