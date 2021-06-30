package com.droid2developers.auction.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.droid2developers.auction.R

class SlideShowAdapter : RecyclerView.Adapter<SlideShowAdapter.ViewHolder>() {
    var images = intArrayOf(
        R.drawable.ic_online_auction,
        R.drawable.ic_auction_sold,
        R.drawable.ic_security_transactions
    )
    var textInfo = intArrayOf(
        R.string.app_info_1,
        R.string.app_info_2,
        R.string.app_info_3
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.slide_show, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var header: TextView = itemView.findViewById<View>(R.id.textInfoId) as TextView
        var imageView: ImageView = itemView.findViewById<View>(R.id.imageId) as ImageView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.header.setText(textInfo[position])
        Glide.with(holder.imageView.context)
            .load(images[position])
            .apply(
                RequestOptions()
                    .centerCrop()
            )
            .into(holder.imageView)
    }

    override fun getItemCount() = images.size
}