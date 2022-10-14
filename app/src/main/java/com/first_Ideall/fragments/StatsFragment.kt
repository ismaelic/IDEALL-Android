package com.first_Ideall.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.first_Ideall.R

class StatsFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Setting the default theme insted of the splash screen before charging page
        inflater.context.setTheme(R.style.Theme_ideall)
        var view = inflater.inflate(R.layout.fragment_stats, container, false)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StatsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}