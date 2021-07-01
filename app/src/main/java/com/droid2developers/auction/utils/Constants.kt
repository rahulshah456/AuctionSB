package com.droid2developers.auction.utils

object Constants {


    const val SKIPPED = "skipped"
    const val COMPLETED = "completed"
    const val REGISTERED = "registered"
    const val NOT_REGISTERED = "notRegistered"
    const val BIDDER_ACCOUNT = "Bidder"
    const val SELLER_ACCOUNT = "Seller"
    const val SKIPPED_ACCOUNT = "Skipped"
    const val PREF_NAME = "Auction_Preferences";
    const val INDIAN_PHONE_CODE = "+91";
    val ACCOUNT_TYPES = arrayOf(1, 2, 3)

    const val DELAY_SLIDE: Long = 5000
    const val PERIOD_SLIDE: Long = 5000
    const val RequestSignInCode = 5

    //intent extras
    const val EXTRA_ACCOUNT_TYPE = "type"
    const val EXTRA_PHONE_NUMBER = "number"
    const val EXTRA_ACCOUNT_DATA = "account_data"
    const val EXTRA_LOGIN_PROGRESS = "progress"

    //firebase constants
    const val DATABASE_REGISTRATIONS = "PhoneRegistrations"
    const val DATABASE_LOCATION_ACCOUNTS = "Accounts"


}