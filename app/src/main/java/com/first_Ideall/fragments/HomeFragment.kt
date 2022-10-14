package com.first_Ideall.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.first_Ideall.R
import com.first_Ideall.activities.NewIdeaActivity
import com.first_Ideall.activities.ProfileActivity
import com.first_Ideall.adapters.PostIdeaAdapter
import com.first_Ideall.model.Post
import com.first_Ideall.model.User
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : Fragment() {

    //Firebase Conection
    private lateinit var auth: FirebaseAuth
    private lateinit var dbFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        //getPosts()
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        return view

    }

    //!once the Fragment is created! Buttons funcionalities
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        // Profile image button
        var firebaseUser = FirebaseAuth.getInstance().currentUser!!

        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid).
            addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val currentUser = snapshot.getValue(User::class.java)

                if(currentUser?.profileImage == ""){
                    iv_homeProfile.setImageResource(R.drawable.addlogo)
                }else{
                    Glide.with(this@HomeFragment).load(currentUser?.profileImage).into(iv_homeProfile)
                }
            }
        })
        iv_homeProfile.setOnClickListener{
            val intent=Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }

        fab_addPost.setOnClickListener {
            val intent = Intent(activity, NewIdeaActivity::class.java)
            startActivity(intent)
        }
    }

    //Getting Posts from Collection in Firebase
    fun getPosts(){
        auth = FirebaseAuth.getInstance()
        dbFirestore = FirebaseFirestore.getInstance()

        dbFirestore.collection("posts").addSnapshotListener { value, error ->
            val posts = value!!.toObjects(Post::class.java)

            posts.forEachIndexed { index, post ->
                post.uid = value.documents[index].id
            }

            rv_posts.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = PostIdeaAdapter(this@HomeFragment, posts)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}