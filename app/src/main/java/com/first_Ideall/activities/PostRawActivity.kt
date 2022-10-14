package com.first_Ideall.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.first_Ideall.R
import com.first_Ideall.model.Post
import kotlinx.android.synthetic.main.activity_raw_post.*
import java.util.*

class PostRawActivity : AppCompatActivity() {

    //Firebase Conection
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raw_post)

        auth = FirebaseAuth.getInstance()
        db =  FirebaseFirestore.getInstance()

        b_post.setOnClickListener{

            val postText = etmt_post.text.toString()
            val date = Date()
            val userName = auth.currentUser!!.displayName

            val post = Post(null,postText, date, userName, null)

            db.collection("posts").add(post)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Posted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener{
                    Toast.makeText(applicationContext, "Fail to post", Toast.LENGTH_SHORT).show()
                }
        }


    }
}