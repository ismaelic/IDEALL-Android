package com.first_Ideall.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.first_Ideall.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import android.content.Intent as Intent
import com.google.firebase.auth.UserProfileChangeRequest




class SignUpActivity : AppCompatActivity(){

    //Firebase variables
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Setting the default theme insted of the splash screen before charging page
        setTheme(R.style.Theme_ideall)
        setContentView(R.layout.activity_sign_up)

        //Firebase Initialization
        auth= FirebaseAuth.getInstance()


        //Validation of values to transmit registration of user in database
        b_signUp.setOnClickListener{
            val userName = et_signUpUserName.text.toString().trim()
            val email = et_signUpEmail.text.toString().trim()
            val password = et_signUpPassword.text.toString().trim()
            val confirmPassword = et_signUpConfirmPassword.text.toString().trim()

            //Required values and others things
            if(TextUtils.isEmpty(userName)){
                Toast.makeText(applicationContext, "Username is required", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext, "Email is required", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext, "Password is required", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.getTrimmedLength(password) < 8){
                Toast.makeText(applicationContext, "Password must have at least 8 characters", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(applicationContext, "Confirm Password is required", Toast.LENGTH_SHORT).show()
            }
            if(password != confirmPassword){
                Toast.makeText(applicationContext, "Password not match", Toast.LENGTH_SHORT).show()
            }

            registerUser(userName,email,password)
        }

        //Button to change to login screen
        b_signUpSignIn.setOnClickListener(){
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }

        //Animation of button signup
        b_signUpSignUp.setOnClickListener{
            b_signUpSignUp.animate().apply{
                duration=1000
                rotationXBy(360F)
            }.start()
        }

    }


    //Register user in Firebase method and next screen
    private fun registerUser(userName:String,email:String,password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {

                if(it.isSuccessful){



                    val user:FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid
                    //Add user diplay name
                    val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(userName).build()
                    user!!.updateProfile(profileUpdates)

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                    //Save data in Map to pass to Database
                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",userName)
                    hashMap.put("profileImage","")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if(it.isSuccessful){
                            //Setting values to blank and opening the new activity
                            et_signUpUserName.setText("")
                            et_signUpEmail.setText("")
                            et_signUpPassword.setText("")
                            et_signUpConfirmPassword.setText("")

                            startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                            finish()
                        }
                    }
                }
            }
    }
}