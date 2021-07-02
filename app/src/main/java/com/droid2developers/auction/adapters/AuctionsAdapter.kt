package com.droid2developers.auction.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droid2developers.auction.R
import com.droid2developers.auction.models.Auction
import com.droid2developers.auction.utils.Constants.HOME_SCREEN_DATE_FORMAT
import com.droid2developers.auction.utils.Constants.ISO_DATE_FORMAT
import com.google.android.material.card.MaterialCardView
import com.orhanobut.logger.Logger
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class AuctionsAdapter : RecyclerView.Adapter<AuctionsAdapter.ItemViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private val auctionsList: MutableList<Auction?> = ArrayList<Auction?>()
    private val df: DateFormat = SimpleDateFormat(ISO_DATE_FORMAT, Locale.ENGLISH)


    interface OnItemClickListener {
        fun onItemClick(cardView: MaterialCardView?, header: TextView?, position: Int)
        fun onItemLikeClick(position: Int)
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var iv_cover: ImageView? = null
        var civ_profile: CircleImageView? = null
        var mcv_main: MaterialCardView? = null
        var tv_count: TextView? = null
        var tv_title: TextView? = null
        var tv_account: TextView? = null

        init {
            iv_cover = itemView.findViewById(R.id.iv_cover)
            civ_profile = itemView.findViewById(R.id.civ_profile)
            mcv_main = itemView.findViewById(R.id.mcv_main)
            tv_count = itemView.findViewById(R.id.tv_count)
            tv_title = itemView.findViewById(R.id.tv_title)
            tv_account = itemView.findViewById(R.id.tv_account)

            mcv_main?.setOnClickListener {
                onItemClickListener?.onItemClick(
                    mcv_main,
                    tv_title,
                    layoutPosition
                )
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_auction_home, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val auction: Auction? = auctionsList[position]
        Logger.d(auction)

        holder.tv_title?.text = auction?.title
        holder.tv_count?.text = String.format(Locale.ENGLISH,
            "Bidding starts at Soon from ₹%d", auction?.startBid)
        holder.tv_account?.text = auction?.authorName


        Glide.with(holder.iv_cover?.context!!)
            .load(auction?.images!![Random(10).nextInt(0, 2)])
            .centerCrop()
            .into(holder.iv_cover!!)
    }


    override fun getItemCount(): Int {
        return auctionsList.size
    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun addAuctions(newAlbumList: List<Auction>) {
        auctionsList.clear()
        auctionsList.addAll(newAlbumList)
        notifyDataSetChanged()
        Logger.d("Size: ${auctionsList.size}")
    }



    fun clearList() {
        if (auctionsList.size > 0) {
            auctionsList.clear()
            notifyDataSetChanged()
        }
    }

    fun getItemList(): List<Auction?> {
        return auctionsList
    }


    fun fromTimestamp(value: Long?): String? {
        if (value != null) {
            try {
                val timeZone = TimeZone.getTimeZone("IST")
                val sdf = SimpleDateFormat(HOME_SCREEN_DATE_FORMAT, Locale.ENGLISH)
                df.timeZone = timeZone
                return sdf.format(df.parse(value.toString())!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return null
    }


}