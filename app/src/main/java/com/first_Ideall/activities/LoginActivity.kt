package com.first_Ideall.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.first_Ideall.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.item_my_ideas.*

class LoginActivity : AppCompatActivity(){

    //Initialize firebase Login vals
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting the default theme insted of the splash screen before charging page
        setTheme(R.style.Theme_ideall)
        setContentView(R.layout.activity_login)

        //initialize the Firebase auth
        auth = FirebaseAuth.getInstance()
        //Automatic Login if you already loged once
        val user:FirebaseUser? = auth.currentUser
        /*
        if(user != null){
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        } */



        //Forgot password link
        var link = findViewById<TextView>(R.id.tv_forgotPassword)
        link.setMovementMethod(LinkMovementMethod.getInstance());


        // login in Firebase and getting to home page
        b_login.setOnClickListener{
            var email = et_loginEmail.text.toString()
            var password = et_loginPassword.text.toString()


            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext, "Email and Password are required", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        if (it.isSuccessful){
                            email = ""
                            password = ""
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()

                        }else{
                            Toast.makeText(applicationContext, "Email or Password are invalid", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        //Change to sign up page
        b_loginSignUp.setOnClickListener(){
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()
        }

        //Login button animations
        b_loginSignIn.setOnClickListener(){
            b_loginSignIn.animate().apply{
                duration=1000
                rotationXBy(360F)
            }.start()
        }
    }
}
