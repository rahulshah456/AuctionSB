package com.droid2developers.auction.models

data class AuctionAccount(
    val uid: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val address: String? = null,
    val country: String? = null,
    val state: String? = null,
    val pinCode: Long? = null,
    val age: Int? = null,
    val aadharUid: String? = null,
    val aadharVerified: Boolean? = null,
    val emailVerified: Boolean? = null,
    val phone: String? = null,
    val profileUrl: String? = null,
    val accountType: Int? = null,
    val biddingRatios: List<BiddingRatio?>? = null,
    val bidParticipations: List<Participation?>? = null,
)