package com.example.bmasa20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class Chat_Activity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var messageAdapter: ChatAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title = "Chat"

        mAuth = FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()

        val currentUser = mAuth.currentUser
        var name = currentUser?.displayName

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        messageList = ArrayList()
        messageAdapter = ChatAdapter(messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter
        EventChangeListener()


        sendButton.setOnClickListener {

            if(TextUtils.isEmpty(messageBox.getText().trim().toString())) {
                messageBox.setError("Description cannot be empty")
                messageBox.requestFocus()
            }
            else{
                val data= hashMapOf(
                    "sendername" to "$name",
                    "message" to messageBox.getText().toString(),
                    "time" to FieldValue.serverTimestamp()
                )

                db.collection("Chat")
                    .add(data)
                    .addOnSuccessListener {docref ->
                        Log.d("Chat Data Addition", "DocumentSnapshot written with ID: ${docref}.id")

                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Chat Data Addition Error adding document", Toast.LENGTH_SHORT).show()
                    }
                val intent = Intent(this,Chat_Activity::class.java)
                startActivity(intent)
                finish()
            }

        }


    }
    private fun EventChangeListener() {

        db= FirebaseFirestore.getInstance()
        db.collection("Chat").orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if(error!=null)
                    {
                        Log.e("Firestore Error",error.message.toString())
                        return
                    }
                    for(dc: DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){

                            messageList.add(dc.document.toObject(Message::class.java))
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                }

            })
    }
}