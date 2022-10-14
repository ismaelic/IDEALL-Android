package com.first_Ideall.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.first_Ideall.R
import com.first_Ideall.model.Post
import kotlinx.android.synthetic.main.activity_new_idea.*
import java.io.IOException
import java.util.*


class NewIdeaActivity : AppCompatActivity() {

    //Firebase Conection
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageRef:StorageReference

    //Variables to get images from galery
    private var filePath : Uri? = null
    private val PICK_IMAGE_REQUEST:Int = 2021


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_idea)

        auth = FirebaseAuth.getInstance()
        db =  FirebaseFirestore.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference

        b_newIdeaPost.setOnClickListener{

            val postTitle = et_ideaName.text.toString()
            val postText = et_ideaDescription.text.toString()
            val date = Date()
            val userName = auth.currentUser!!.displayName
            var postImg = ""

            if(postTitle != "" && userName != null && postText != ""){
                if (filePath != null){
                    postImg = uploadImage(postTitle,userName)
                }


                val post = Post(postTitle,postText, date, userName, postImg)

                db.collection("posts").add(post)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Posted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener{
                        Toast.makeText(applicationContext, "Fail to post", Toast.LENGTH_SHORT).show()
                    }
            }else{

                if(TextUtils.isEmpty(postTitle)){
                    Toast.makeText(applicationContext, "Idea name is required", Toast.LENGTH_SHORT).show()
                }
                if(TextUtils.isEmpty(postText)){
                    Toast.makeText(applicationContext, "Description is required", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Upload image
        iv_ideaImage.setOnClickListener{
            chooseImage()
        }
        b_newIdeaPhoto.setOnClickListener{
            chooseImage()
        }

        // New Idea button
        b_newIdeaSave.setOnClickListener{
            startActivity(Intent(this, IdeaActivity::class.java))
        }

    }

    //open the image galery
    private fun chooseImage(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)

    }

    //Get from galery and put image in profile image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_REQUEST && data != null){
            filePath = data.data

            try {
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                iv_ideaImage.setImageBitmap(bitmap)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    //Uploading the image selected to the storage of Firebase
    private fun uploadImage(postTitle : String, userName : String): String {

        var url = ""

        var ref: StorageReference = storageRef.child("image/"+
                userName + postTitle)
        ref.putFile(filePath!!)
            .addOnSuccessListener {
                url = it.toString()
                Toast.makeText(applicationContext,"Uploaded",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(applicationContext,"Failed upload"+ it.message,Toast.LENGTH_SHORT).show()
            }

        return url
    }

}

