package com.example.bmasa20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class Availability_Activity : AppCompatActivity() {

    private lateinit var db:FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_availability)

        db= FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        val radioGroup = findViewById<RadioGroup>(R.id.rg)
        val des = findViewById<TextInputEditText>(R.id.etcost_et)
        val notify = findViewById<Button>(R.id.button)
        var name = currentUser?.displayName

        radioGroup.setOnCheckedChangeListener{ radioGroup,i ->
            var rb = findViewById<RadioButton>(i)
            notify.setOnClickListener {
                if(rb!=null)
                {
                    if (TextUtils.isEmpty(des.getText()?.trim().toString())){
                        des.setError("Description cannot be empty")
                        des.requestFocus()
                    }
                    else{

                        val data= hashMapOf(
                            "name" to "$name",
                            "status" to rb.text.toString(),
                            "description" to des.text.toString(),
                            "date" to FieldValue.serverTimestamp()
                        )

                        db.collection("Users").document("$name")
                            .set(data)
                            .addOnSuccessListener {docref ->
                                Log.d("Data Addition", "DocumentSnapshot written with ID: ${docref}.id")
                                val intent = Intent(this,Home_Activity::class.java)
                                Toast.makeText(this,"Notified", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                                finishAffinity()
                            }
                            .addOnFailureListener { e ->
                                Log.w("Data Addition", "Error adding document", e)
                            }
                    }

                }
                else{

                    Toast.makeText(this,"Select a Status",Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}