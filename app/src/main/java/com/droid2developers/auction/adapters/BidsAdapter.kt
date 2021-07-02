package com.droid2developers.auction.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.droid2developers.auction.R
import com.droid2developers.auction.models.Auction
import com.droid2developers.auction.models.Bid
import com.orhanobut.logger.Logger
import java.util.*

class BidsAdapter : RecyclerView.Adapter<BidsAdapter.ItemViewHolder>()  {


    private val bidsList: MutableList<Bid?> = ArrayList<Bid?>()


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_price: TextView? = null
        var tv_timestamp: TextView? = null
        var tv_account: TextView? = null

        init {
            tv_price = itemView.findViewById(R.id.tv_price)
            tv_timestamp = itemView.findViewById(R.id.tv_timestamp)
            tv_account = itemView.findViewById(R.id.tv_account)
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bid, parent, false)
        return ItemViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val bid: Bid? = bidsList[position]
        Logger.d(bid)

        holder.tv_account?.text = bid?.bidderName
        holder.tv_price?.text = bid?.bidValue?.toString()
        holder.tv_timestamp?.text = bid?.timeStamp?.toString()
    }


    override fun getItemCount(): Int {
        return bidsList.size
    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun addBids(newAlbumList: List<Bid>) {
        bidsList.clear()
        bidsList.addAll(newAlbumList)
        notifyDataSetChanged()
        Logger.d("Size: ${bidsList.size}")
    }



    fun clearList() {
        if (bidsList.size > 0) {
            bidsList.clear()
            notifyDataSetChanged()
        }
    }

    fun getItemList(): List<Bid?> {
        return bidsList
    }

}