package com.droid2developers.auction.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.droid2developers.auction.R

class DescriptionFragment: Fragment() {

    companion object {
        fun newInstance(desc: String) = DescriptionFragment().apply {
            arguments = Bundle(2).apply {
                putString("desc", desc)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvDesc = view.findViewById<TextView>(R.id.tv_desc)

        val bundle = arguments
        if (bundle != null) {
            val description = bundle.getString("desc")
            if (description != null) {
                tvDesc.text = description
            }
        }

    }

}