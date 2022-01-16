package com.example.bmasa20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val userlist:ArrayList<Users>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.user_view, parent ,false)
        return MyViewHolder(view)
    }



    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val user : Users = userlist[position]
        holder.title2.text = user.name
        holder.status.text = user.status
        holder.message.text= user.description
        holder.timestamp.text = user.date?.toDate().toString()
    }

    override fun getItemCount(): Int {
        val size = userlist.size
        return size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val message = itemView.findViewById<TextView>(R.id.message)
        val title2= itemView.findViewById<TextView>(R.id.title2)
        val status = itemView.findViewById<TextView>(R.id.status)
        val timestamp = itemView.findViewById<TextView>(R.id.timestamp)

    }

}