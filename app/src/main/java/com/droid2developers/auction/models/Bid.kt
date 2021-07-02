package com.droid2developers.auction.models

data class Bid(
    val id: String? = null,
    val bidderId: String? = null,
    val auctionId: String? = null,
    val bidderName: String? = null,
    val bidValue: Int? = null,
    val timeStamp: Long? = null,
    val isEarlyBid: Boolean? = null,
    val isLateBid: Boolean? = null,
    val successiveOutbidding: Boolean? = null,
)