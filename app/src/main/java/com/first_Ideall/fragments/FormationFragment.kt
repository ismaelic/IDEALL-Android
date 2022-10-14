package com.first_Ideall.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.first_Ideall.R

class FormationFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Setting the default theme insted of the splash screen before charging page
        inflater.context.setTheme(R.style.Theme_ideall)

        return inflater.inflate(R.layout.fragment_formation, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FormationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}