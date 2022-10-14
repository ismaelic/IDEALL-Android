package com.first_Ideall.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.first_Ideall.R
import com.first_Ideall.fragments_profile.PresentProfileFragment
import com.first_Ideall.fragments_profile.IdeallProfileFragment
import com.first_Ideall.model.User
import kotlinx.android.synthetic.main.activity_idea.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.fragment_users.view.*
import java.io.IOException
import java.util.*

class ProfileActivity : AppCompatActivity() {

    //Firebase variables
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage:FirebaseStorage
    private lateinit var storageRef:StorageReference

    //Variables to get images from galery
    private var filePath : Uri? = null
    private val PICK_IMAGE_REQUEST:Int = 2021

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Setting the default theme insted of the splash screen before charging page
        setTheme(R.style.Theme_ideall)
        setContentView(R.layout.activity_profile)

        //Getting profile database
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        //no suggestion on edit text
        et_profileUserName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val currentUser = snapshot.getValue(User::class.java)
                et_profileUserName.setText(currentUser!!.userName)

                if(currentUser.profileImage == ""){
                    iv_profileUserImage.setImageResource(R.drawable.user_profile_image)
                }else{
                    Glide.with(this@ProfileActivity).load(currentUser.profileImage).into(iv_profileUserImage)

                }
            }
        })

        //Getting image from Galery and uploading it to Firebase

        iv_profileUserImage.setOnClickListener{
            chooseImage()
        }
        b_profileSave.setOnClickListener{
            uploadImage()
            pb_profileImage.visibility = View.VISIBLE
        }

        // Back button
        iv_profileBack.setOnClickListener{
            val intent= Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

        //Fragments in the page
        val adapter = IdeaActivity.MyViewPagerFragmentAdapter(supportFragmentManager)
        adapter.addFragment(PresentProfileFragment(), "Personal")
        adapter.addFragment(IdeallProfileFragment(), "Professional")

        vp_profile.adapter = adapter
        tabProfile.setupWithViewPager(vp_profile)

        //Setting icons in layout
        tabProfile.getTabAt(0)?.setIcon(R.drawable.logo1)
        tabProfile.getTabAt(1)?.setIcon(R.drawable.logo2)

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
            filePath = data!!.data

            try {
                var bitmap:Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                iv_profileUserImage.setImageBitmap(bitmap)
                b_profileSave.visibility= View.VISIBLE
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }

    //Uploading the image selected to the storage of Firebase
    private fun uploadImage(){

        if(filePath != null){

            var ref:StorageReference = storageRef.child("image/"+firebaseUser.displayName)
            ref.putFile(filePath!!)
                .addOnSuccessListener {

                    //Getting updated data of name and image and updating in Firebase
                    it.storage.downloadUrl.addOnCompleteListener {
                        val hashMap:HashMap<String,String> = HashMap()
                        hashMap.put("userName",et_profileUserName.text.toString())
                        hashMap.put("profileImage",it.result.toString())
                        databaseReference.updateChildren(hashMap as Map<String, Any>)
                    }

                    pb_profileImage.visibility = View.GONE
                    b_profileSave.visibility= View.GONE
                    Toast.makeText(applicationContext,"Uploaded",Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener{
                    pb_profileImage.visibility = View.GONE
                    Toast.makeText(applicationContext,"Failed upload"+ it.message,Toast.LENGTH_SHORT).show()
                }
        }
    }
}