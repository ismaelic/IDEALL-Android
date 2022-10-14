package com.first_Ideall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.first_Ideall.R
import com.first_Ideall.adapters.MyIdeasAdapter
import com.first_Ideall.databinding.ActivityMyIdeasBinding
import com.first_Ideall.listeners.IdeaClickListener
import com.first_Ideall.model.IDEA_ID_EXTRA
import com.first_Ideall.model.Idea
import com.first_Ideall.model.ideaList
import kotlinx.android.synthetic.main.activity_my_ideas.*
import java.util.*
import kotlin.collections.ArrayList

class MyIdeasActivity : AppCompatActivity(), IdeaClickListener {

    private lateinit var binding :ActivityMyIdeasBinding
    val displayList = ArrayList<Idea>()
    //Firebase variables
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyIdeasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pupulateIdeas()
        displayList.addAll(ideaList)

        //Inflate recycler view whit Cardviews
        val myIdeasActivity = this
        binding.rvMyIdeas.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = MyIdeasAdapter(displayList, myIdeasActivity)

        }


    }

    override fun onClick(idea: Idea) {
        val intent = Intent(applicationContext,IdeaActivity::class.java)
        intent.putExtra(IDEA_ID_EXTRA,idea.id)
        startActivity(intent)

    }

    private fun pupulateIdeas() {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        ideaList.add(Idea(name = "hola", image = R.drawable.logo, user = firebaseUser.uid))
        ideaList.add(Idea(name = "adions", image = R.drawable.lights, user = firebaseUser.uid))
        ideaList.add(Idea(name = "imagen", image = R.drawable.lake, user = firebaseUser.uid))
        ideaList.add(Idea(name = "nada", image = R.drawable.garidy_profile, user = firebaseUser.uid))
        ideaList.add(Idea(name = "imagen1", image = R.drawable.city_splash, user = firebaseUser.uid))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_my_ideas,menu)
        val menuItem = menu!!.findItem(R.id.searchIdeas)

        if(menuItem != null){
            val searchView = menuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(newText: String?): Boolean {
                     return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    displayList.clear()
                    if(newText!!.isNotEmpty()){
                        val search = newText.lowercase(Locale.getDefault())
                        ideaList.forEach{
                            if(it.name.lowercase(Locale.getDefault()).contains(search)){
                                displayList.add(it)
                            }
                        rv_myIdeas.adapter!!.notifyDataSetChanged()

                        }
                    }else{
                        displayList.addAll(ideaList)
                        rv_myIdeas.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }

            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}