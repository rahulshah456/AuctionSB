package com.droid2developers.auction.models

data class PhoneRegistrations(
    val number: String? = null,
    val accountType: Int? = -1,
    val isBlocked: Boolean? = false,
)