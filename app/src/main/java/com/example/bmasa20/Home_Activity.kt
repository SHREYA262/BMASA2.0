package com.example.bmasa20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class Home_Activity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    private lateinit var userlist : ArrayList<Users>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var adapter : MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()

        drawerLayout = findViewById(R.id.drawer)
        val navView : NavigationView = findViewById(R.id.navView)


        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //This line displays hamburger icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val recyclerView =  findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        userlist = arrayListOf()
        adapter = MyAdapter(userlist)
        recyclerView.adapter = adapter
        var name = mAuth.currentUser?.displayName
        EventChangeListener(name)


        navView.setNavigationItemSelectedListener {

            it.isChecked = true

            when(it.itemId)
            {
                R.id.nav_home->{
                    Toast.makeText(applicationContext,"Clicked Home", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Home_Activity::class.java))
                    finishAffinity()
                }
                R.id.nav_chat->{
                    Toast.makeText(applicationContext,"Clicked Chat", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Chat_Activity::class.java))
                    drawerLayout.closeDrawers()
                }
                R.id.nav_profile-> {
                    startActivity(Intent(this,Profile_Activity::class.java))
                }
                R.id.nav_logout->{
                    Toast.makeText(applicationContext,"User logged out ", Toast.LENGTH_SHORT).show()
                    mAuth.signOut()
                    val intent = Intent(this,Login_Activity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    finish()
                }

                R.id.nav_availability-> {
                    Toast.makeText(applicationContext,"Clicked Availability", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Availability_Activity::class.java))
                    drawerLayout.closeDrawers()
                }

                R.id.settings->{
                    Toast.makeText(applicationContext,"Clicked Settings", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawers()

                }
            }
            true
        }



    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){

            return true

        }
        return super.onOptionsItemSelected(item)
    }

    private fun EventChangeListener(page_name:String?=null) {

        db= FirebaseFirestore.getInstance()
        db.collection("Users").orderBy("name", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if(error!=null)
                    {
                        Log.e("Firestore Error",error.message.toString())
                        return
                    }
                    for(dc: DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){

                            userlist.add(dc.document.toObject(Users::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

            })

}
}