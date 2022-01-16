package com.example.bmasa20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class Profile_Activity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        val user_name = findViewById<TextView>(R.id.user_name)
        user_name .text = currentUser?.displayName

        val user_email  = findViewById<TextView>(R.id.user_email)
        user_email.text = currentUser?.email

        val profile_pic = findViewById<ImageView>(R.id.profile_pic)
        Glide.with(this).load(currentUser?.photoUrl).into(profile_pic)

        val logout_btn = findViewById<Button>(R.id.logout_btn)
        logout_btn?.setOnClickListener()
        {
            mAuth.signOut()
            val intent = Intent(this,Login_Activity::class.java)
            startActivity(intent)
            finish()
        }

    }
}