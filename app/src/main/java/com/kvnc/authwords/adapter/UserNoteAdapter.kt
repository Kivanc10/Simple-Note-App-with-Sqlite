package com.kvnc.authwords.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kvnc.authwords.Inside
import com.kvnc.authwords.LoginPage
import com.kvnc.authwords.R
import com.kvnc.authwords.SetNote
import com.kvnc.authwords.model.User

class UserNoteAdapter (private val context: Context,private val dataSet : MutableList<String>,private val user: User) : RecyclerView.Adapter<UserNoteAdapter.UserNoteViewHolder>(){

    class UserNoteViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        val userNoteHolder : TextView = view.findViewById(R.id.noteHolderText)
        val textViewNote : TextView = view.findViewById(R.id.userNameTextInside)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserNoteViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view,parent,false)
        return UserNoteViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: UserNoteViewHolder, position: Int) {
        val item = dataSet[position]
        holder.userNoteHolder.text = item

        holder.textViewNote.text = this.user.userName
        holder.userNoteHolder.setOnClickListener {
            val tempText = holder.userNoteHolder.text
            val context = holder.itemView.context
            //Inside.NOTE

            val intent = Intent(context,SetNote::class.java)
            intent.putExtra(Inside.NOTE,tempText)
            intent.putExtra(Inside.USER_NAME,this.user.userName)
            intent.putExtra(LoginPage.LOGIN_USER,this.user.userName)
            intent.putExtra(LoginPage.LOGIN_USER_MAIL,this.user.password)
            context.startActivity(intent)



            Toast.makeText(holder.itemView.context,tempText, Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}