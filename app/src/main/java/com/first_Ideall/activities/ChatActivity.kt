package com.first_Ideall.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.first_Ideall.R
import com.first_Ideall.adapters.ChatAdapter
import com.first_Ideall.model.Chat
import com.first_Ideall.model.User
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.fragment_users.view.*
import kotlinx.android.synthetic.main.item_users.*

class ChatActivity : AppCompatActivity() {

    //Firebase variables
    var firebaseUser:FirebaseUser? = null
    var reference:DatabaseReference? = null
    var chatList = ArrayList<Chat>()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Setting the default theme insted of the splash screen before charging page
        setTheme(R.style.Theme_ideall)
        setContentView(R.layout.activity_chat)

        rv_chats.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)

        var intent = getIntent()
        var userId = intent.getStringExtra("userId")

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        reference!!.addValueEventListener(object:ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                val user = snapshot.getValue(User::class.java)
                tv_chatUserName.text = user!!.userName

                if(user.profileImage.equals("")){
                    iv_chatProfileImg.setImageResource(R.drawable.user_profile_image)
                }else{
                    Glide.with(this@ChatActivity).load(user.profileImage).into(iv_chatProfileImg)
                }
            }
        })

        // Back button
        iv_chatBack.setOnClickListener{
            onBackPressed()
        }
        //Send Button
        b_sendMessage.setOnClickListener{
            var message:String = et_message.text.toString()

            if(message.isEmpty()){
                Toast.makeText(applicationContext, "Message is empty", Toast.LENGTH_SHORT).show()
                et_message.setText("")
            }else{
                //Send messsage
                sendMessage(firebaseUser!!.uid,userId,message)
                et_message.setText("")
            }

        }

        readMessage(firebaseUser!!.uid,userId)


    }
    //Send message to the database and store it
     private fun sendMessage (senderId:String, recieverId:String, message:String){
         var reference:DatabaseReference? = FirebaseDatabase.getInstance().getReference()
         var hashMap:HashMap<String,String> = HashMap()
         hashMap.put("senderId",senderId)
         hashMap.put("recieverId",recieverId)
         hashMap.put("message",message)

         reference!!.child("Chat").push().setValue(hashMap)
     }

    //Getting the chats that already existed
    fun readMessage(senderId:String,recieverId: String){

        val databaseReference:DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
            }
            //Adding chat to list
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat != null) {
                        if(chat.senderId.equals(senderId) && chat.recieverId.equals(recieverId) ||
                            chat.senderId.equals(recieverId) && chat.recieverId.equals(senderId)){
                            chatList.add(chat)
                        }
                    }
                }
                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)
                rv_chats.adapter = chatAdapter

            }
        })
    }
}
