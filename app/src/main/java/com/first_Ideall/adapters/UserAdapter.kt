package com.first_Ideall.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.first_Ideall.R
import com.first_Ideall.fragments.UsersFragment
import com.first_Ideall.model.User

class UserAdapter(private val context: UsersFragment, private val userList:ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    //Gets how many users are
    override fun getItemCount(): Int {
        return userList.size
    }

    //Gives the users data to be put in the item_chat_users Layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return ViewHolder(view)
    }

    //Gets the image of the user
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList [position]
        holder.userName.text = user.userName
        Glide.with(context).load(user.profileImage).placeholder(R.drawable.user_profile_image).into(holder.userImg)

        holder.layoutUser.setOnClickListener {
            //Weird action it dose not accept context
            val intent = Intent(context.context, com.first_Ideall.activities.ChatActivity::class.java)
            intent.putExtra("userId",user.userId)
            context.startActivity(intent)

        }
    }

    //Creating a adapter for the recycler view items
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val userName:TextView = view.findViewById(R.id.tv_userName)
        //val userTemp:TextView = view.findViewById(R.id.tv_userTemp)
        val userImg:ImageView = view.findViewById(R.id.tv_userImage)
        val layoutUser:LinearLayout = view.findViewById(R.id.layout_itemUsers)


    }

}