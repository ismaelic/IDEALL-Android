package com.first_Ideall.fragments_idea

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.first_Ideall.R
import kotlinx.android.synthetic.main.fragment_forth.*
import kotlinx.android.synthetic.main.line_text.view.*

class ForthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineTextData()
    }

    fun lineTextData(){
        var includedLayout: View
        var insideTheIncludedLayout: TextView

        //Results
        includedLayout = lineResults
        insideTheIncludedLayout = includedLayout.text as TextView
        insideTheIncludedLayout.text = "Results"


        //Moments
        includedLayout = lineMoments
        insideTheIncludedLayout = includedLayout.text as TextView
        insideTheIncludedLayout.text = "Key Moments"

        //Moments
        includedLayout = lineChanges
        insideTheIncludedLayout = includedLayout.text as TextView
        insideTheIncludedLayout.text = "New Changes"
    }
}