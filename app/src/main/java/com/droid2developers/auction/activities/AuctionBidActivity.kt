package com.droid2developers.auction.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.droid2developers.auction.R
import com.droid2developers.auction.adapters.TabAdapter
import com.droid2developers.auction.fragments.BiddingFragment
import com.droid2developers.auction.fragments.DescriptionFragment
import com.droid2developers.auction.models.Auction
import com.droid2developers.auction.utils.Constants
import com.droid2developers.auction.utils.Constants.DATABASE_BIDS
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.logger.Logger
import kotlin.random.Random


class AuctionBidActivity : AppCompatActivity() {

    private var placeBidBtn: MaterialCardView? = null
    private var tabAdapter: TabAdapter? = null
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    private var toolbar: MaterialToolbar? = null
    private var tvAccount: TextView? = null
    private var iv_cover: ImageView? = null
    private var codeEditText: TextInputEditText? = null

    private var auction: Auction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction_bid)


        viewPager = findViewById(R.id.viewPagerId)
        tabAdapter = TabAdapter(supportFragmentManager)
        tabLayout = findViewById(R.id.tabLayoutId)
        toolbar = findViewById(R.id.mtv_id)
        tvAccount = findViewById(R.id.tv_account)
        iv_cover = findViewById(R.id.iv_cover)
        codeEditText = findViewById(R.id.confirmEditTextID)
        placeBidBtn = findViewById(R.id.mcv_continue)

        val bundle = intent.extras
        if (bundle != null) {
            val auctionId = bundle.getString("id")
            if (auctionId != null) {
                fetchAuctionDetails(auctionId)
            }
        }


        placeBidBtn?.setOnClickListener {
            if (!TextUtils.isEmpty(codeEditText?.text)) {
                val bid = codeEditText?.text.toString().trim { it <= ' ' }
                placeBidForAuction(bid.toInt())
            } else {
                Toast.makeText(
                    this@AuctionBidActivity,
                    "Please provide correct Verification Code!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }


    private fun placeBidForAuction(bidValue: Int?) {

        val db = FirebaseFirestore.getInstance()
        val bidsRef = db.collection(DATABASE_BIDS)
        val auth = FirebaseAuth.getInstance()

        val ref = bidsRef.document()
        val data = hashMapOf(
            "id" to ref.id,
            "bidderId" to auth.uid,
            "auctionId" to auction?.id,
            "bidderName" to auth.currentUser?.displayName,
            "bidValue" to bidValue,
            "timeStamp" to System.currentTimeMillis()
        )
        ref.set(data)
    }


    private fun fetchAuctionDetails(auctionId: String) {

        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.DATABASE_AUCTIONS)
            .document(auctionId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Logger.d("${document.id} => ${document.data}")
                    auction = document.toObject(Auction::class.java)
                    if (auction!= null) {
                        updateViews(auction!!)
                    }
                }
            }
    }

    private fun updateViews(auction: Auction) {
        tabAdapter?.addFragment(DescriptionFragment.newInstance(auction.desc!!), "Description")
        tabAdapter?.addFragment(BiddingFragment.newInstance(auction.id!!, false), "Bids")
        viewPager?.adapter = tabAdapter
        tabLayout?.setupWithViewPager(viewPager)

        toolbar?.title = auction.title
        tvAccount?.text = auction.authorName

        Glide.with(applicationContext)
            .load(auction.images!![Random.nextInt(0, 2)])
            .centerCrop()
            .into(iv_cover!!)
    }
}