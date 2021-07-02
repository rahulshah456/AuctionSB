package com.droid2developers.auction.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.droid2developers.auction.R
import com.droid2developers.auction.adapters.BidsAdapter
import com.droid2developers.auction.models.Bid
import com.droid2developers.auction.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.logger.Logger

class BiddingFragment: Fragment() {

    private var recyclerView: RecyclerView? = null
    private var bidsAdapter: BidsAdapter? = null

    companion object {
        fun newInstance(value: String, isUser: Boolean) = BiddingFragment().apply {
            arguments = Bundle(2).apply {
                putString("value", value)
                putBoolean("isUser", isUser)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragmant_bidding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_main)

        generateRecyclerView()
        val bundle = arguments
        if (bundle != null) {
            val isUser = bundle.getBoolean("isUser", true)
            val value = bundle.getString("value")
            Logger.d(isUser)
            Logger.d(value)
            if (isUser) {
                getBidsForUser()
            } else {
                getBidsForAuction(value!!)
            }
        }

    }


    private fun generateRecyclerView() {
        // Set up RecyclerView
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.setItemViewCacheSize(20)

        // Adapter initialization
        bidsAdapter = BidsAdapter()
        recyclerView?.adapter = bidsAdapter
    }


    private fun getBidsForAuction(auctionId: String) {

        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.DATABASE_BIDS)
            .whereEqualTo("auctionId", auctionId)
            .get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val auctions = arrayListOf<Bid>()
                    for (document in result) {
                        Logger.d("${document.id} => ${document.data}")
                        auctions.add(document.toObject(Bid::class.java))
                    }
                    bidsAdapter?.addBids(auctions)
                }

            }
            .addOnFailureListener { exception ->
                Logger.w("Error getting documents: ", exception)
            }

    }


    private fun getBidsForUser() {

        val database = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        database.collection(Constants.DATABASE_BIDS)
            .whereEqualTo("bidderId", auth.uid)
            .get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val auctions = arrayListOf<Bid>()
                    for (document in result) {
                        Logger.d("${document.id} => ${document.data}")
                        auctions.add(document.toObject(Bid::class.java))
                    }
                    bidsAdapter?.addBids(auctions)
                }

            }
            .addOnFailureListener { exception ->
                Logger.w("Error getting documents: ", exception)
            }

    }

}