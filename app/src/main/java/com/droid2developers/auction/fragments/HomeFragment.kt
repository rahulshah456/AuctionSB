package com.droid2developers.auction.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droid2developers.auction.R
import com.droid2developers.auction.activities.AuctionBidActivity
import com.droid2developers.auction.adapters.AuctionsAdapter
import com.droid2developers.auction.models.Auction
import com.droid2developers.auction.utils.Constants
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.logger.Logger
import java.util.*

class HomeFragment: Fragment() {

    private var auctionsRecyclerView: RecyclerView? = null
    private var auctionsAdapter: AuctionsAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auctionsRecyclerView = view.findViewById(R.id.rv_main)

        generateRecyclerView()
        getAuctionsData()
    }

    private fun generateRecyclerView() {
        // Set up RecyclerView
        auctionsRecyclerView?.layoutManager = LinearLayoutManager(context)
        auctionsRecyclerView?.itemAnimator = DefaultItemAnimator()
        auctionsRecyclerView?.setItemViewCacheSize(20)

        // Adapter initialization
        auctionsAdapter = AuctionsAdapter()
        auctionsRecyclerView?.adapter = auctionsAdapter
        auctionsAdapter?.setOnItemClickListener(object : AuctionsAdapter.OnItemClickListener {

            override fun onItemClick(cardView: MaterialCardView?, header: TextView?, position: Int) {
                //collection
                val auction = auctionsAdapter?.getItemList()?.get(position)

                //intent
                val intent = Intent(context, AuctionBidActivity::class.java)
                intent.putExtra("id", auction?.id)
                val options = ActivityOptionsCompat
                    .makeClipRevealAnimation(
                        cardView!!, 0, 0, cardView.measuredWidth,
                        cardView.measuredHeight
                    )
                startActivity(intent, options.toBundle())
            }

            override fun onItemLikeClick(position: Int) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun getAuctionsData() {

        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.DATABASE_AUCTIONS).get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val auctions = arrayListOf<Auction>()
                    for (document in result) {
                        Logger.d("${document.id} => ${document.data}")
                        auctions.add(document.toObject(Auction::class.java))
                    }
                    auctionsAdapter?.addAuctions(auctions)
                }

            }
            .addOnFailureListener { exception ->
                Logger.w("Error getting documents: ", exception)
            }

    }

}