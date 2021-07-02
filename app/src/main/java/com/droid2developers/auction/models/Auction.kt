package com.droid2developers.auction.models

data class Auction(
    val id: String? = null,
    val title: String? = null,
    val desc: String? = null,
    val authorId: String? = null,
    val authorName: String? = null,
    val images: List<String?>? = null,
    val duration: Long? = null,
    val category: String? = null,
    val startTime: Long? = null,
    val winnerId: String? = null,
    val status: Int? = null,
    val startBid: Int? = null,
    val minIncreaseBid: Int? = null,
    val participations: List<String?>? = null
)