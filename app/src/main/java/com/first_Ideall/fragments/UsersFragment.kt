package com.first_Ideall.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.first_Ideall.R
import com.first_Ideall.activities.HomeActivity
import com.first_Ideall.activities.ProfileActivity
import com.first_Ideall.adapters.UserAdapter
import com.first_Ideall.model.User
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.fragment_users.view.*

class UsersFragment : Fragment() {

    //initializing variables
    var userList = ArrayList<User>()

    companion object {
        @JvmStatic
        fun newInstance() =
            UsersFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Setting the default theme insted of the splash screen before charging page
        inflater.context.setTheme(R.style.Theme_ideall)
        var view = inflater.inflate(R.layout.fragment_users, container, false)
        return view
    }

    //Setting the data on the Recycled View !once the Fragment is created!
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        //getting users and setting recyclerview
        getUserList()

        rv_users.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = UserAdapter(this@UsersFragment,userList)

        }

        // Back button
        itemView.iv_userBack.setOnClickListener{
            val intent=Intent(activity,HomeActivity::class.java)
            startActivity(intent)
        }

        // Profile image button
        itemView.iv_userProfileImg.setOnClickListener{
            val intent=Intent(activity,ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    //Getting data from database to set it in Recyclerview list of users
    fun getUserList(){
        val firebase = FirebaseAuth.getInstance().currentUser!!
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(getActivity(), error.message, Toast.LENGTH_SHORT).show()

            }
            //Adding users to list
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(User::class.java)

                    if(!user!!.userId.equals(firebase.uid)){
                        userList.add(user)
                    }
                }
            }
        }
        )
    }
}


/* UNUSED Refreshing fragment to show up the list
    fun refreshFragment(context: Context){
        context?.let {
            val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
            fragmentManager?.let {
                val currentFragment = fragmentManager.findFragmentById(R.id.fl_menu)
                currentFragment?.let {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.detach(it)
                    fragmentTransaction.attach(it)
                    fragmentTransaction.commit()
                }
            }
        }
    }*/