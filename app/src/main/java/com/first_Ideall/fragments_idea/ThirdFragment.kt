package com.first_Ideall.fragments_idea

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.first_Ideall.activities.MindMapActivity
import com.first_Ideall.R
import com.first_Ideall.rooms.data.IdeaData
import com.first_Ideall.rooms.view_model.IdeaViewModel
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.fragment_third.view.*


class ThirdFragment : Fragment() {

    private val ideaViewModel: IdeaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_third, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // My MIND MAP Ideas button
        view.b_openMindMap.setOnClickListener{
            setFabListener()
        }
    }

    private fun setFabListener() {
        b_openMindMap.setOnClickListener {

            val date = System.currentTimeMillis()
            val title = getString(R.string.new_mind_map)
            val newIdea = IdeaData(null, title, date, date, true, null)
            ideaViewModel.insert(newIdea) { groupId ->
                val intent = Intent(activity, MindMapActivity::class.java)
                intent.putExtra("groupId", groupId)
                intent.putExtra("isNewIdea", true)
                startActivity(intent)
            }
        }
    }
}