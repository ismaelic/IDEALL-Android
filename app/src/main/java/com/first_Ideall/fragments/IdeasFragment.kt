package com.first_Ideall.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.first_Ideall.R
import com.first_Ideall.activities.ExploreActivity
import com.first_Ideall.activities.MyIdeasActivity
import com.first_Ideall.activities.NewIdeaActivity
import kotlinx.android.synthetic.main.fragment_ideas.view.*

class IdeasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Setting the default theme insted of the splash screen before charging page
        inflater.context.setTheme(R.style.Theme_ideall)
        return inflater.inflate(R.layout.fragment_ideas, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            IdeasFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    //!once the Fragment is created! Buttons funcionalities
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)


        // New Idea button
        itemView.b_newIdea.setOnClickListener{
            val intent= Intent(activity, NewIdeaActivity::class.java)
            startActivity(intent)
        }

        itemView.ib_explore.setOnClickListener{
            val intent= Intent(activity, ExploreActivity::class.java)
            startActivity(intent)
        }


        // My Ideas button
        itemView.ib_myIdeas.setOnClickListener{
            val intent= Intent(activity, MyIdeasActivity::class.java)
            startActivity(intent)
        }

        // Popular IDEAS button
        itemView.b_newIdea.setOnClickListener{
            val intent= Intent(activity, NewIdeaActivity::class.java)
            startActivity(intent)
        }

        // RAWS button
        itemView.b_newIdea.setOnClickListener{
            val intent= Intent(activity, NewIdeaActivity::class.java)
            startActivity(intent)
        }

        // ??? button
        itemView.b_newIdea.setOnClickListener{
            val intent= Intent(activity, NewIdeaActivity::class.java)
            startActivity(intent)
        }

    }
}