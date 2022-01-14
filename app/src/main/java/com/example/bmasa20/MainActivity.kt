package com.example.bmasa20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth =FirebaseAuth.getInstance()
        val user=mAuth.currentUser

        supportActionBar?.hide()
        Handler().postDelayed({
            if(user != null){
                val dashboardIntent= Intent(this,MainActivity3::class.java)
                startActivity(dashboardIntent)
                finish()
            }
            else{
            val signInIntent = Intent(this,MainActivity2::class.java)
            startActivity(signInIntent)}
            finish()
        },2000)
    }
}