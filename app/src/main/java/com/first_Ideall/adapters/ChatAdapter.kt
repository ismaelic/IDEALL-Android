package com.first_Ideall.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.first_Ideall.R
import com.first_Ideall.model.Chat

class ChatAdapter(private val context: Context, private val chatList:ArrayList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    //Chats variables
    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser: FirebaseUser? = null

    //Puts the chats in the layout and give them the drawables
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == MESSAGE_TYPE_RIGHT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_right, parent, false)
            return ViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_left, parent, false)
            return ViewHolder(view)
        }
    }

    //Gets how many users are
    override fun getItemCount(): Int {
        return chatList.size
    }

    //Gets the image of the user
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList [position]
        holder.message.text = chat.message
        //Glide.with(context).load(chat.profileImage).placeholder(R.drawable.user_profile_image).into(holder.userImg)

    }

    //Creating a adapter for the recycler view chats
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val message:TextView = view.findViewById(R.id.tv_message)
       // val userImg:ImageView = view.findViewById(R.id.tv_userImage)
    }

    //Setting the side of the messages
    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (chatList[position].senderId == firebaseUser!!.uid){
            return MESSAGE_TYPE_RIGHT
        }else{
            return MESSAGE_TYPE_LEFT
        }

    }

}