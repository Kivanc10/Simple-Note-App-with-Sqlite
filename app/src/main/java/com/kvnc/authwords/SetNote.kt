package com.kvnc.authwords

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.kvnc.authwords.model.DBOpenHelper

class SetNote : AppCompatActivity() {
     lateinit var userNameReal : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_note)
        title = "Update & Delete Note"

        val updateBtn : Button = findViewById(R.id.setNoteUpdateBtn)
        val deleteBtn : Button = findViewById(R.id.setNoteDeleteBtn)

        val closeBtn : Button = findViewById(R.id.closeTheSetTextBtn)

        val noteFromUser = intent.getStringExtra(Inside.NOTE)
        val userName = intent.getStringExtra(Inside.USER_NAME)
        userNameReal = userName.toString()

        val editNoteText : TextInputEditText = findViewById(R.id.setNoteEditText)

        editNoteText.setText(noteFromUser)

        val dbHelper = DBOpenHelper(this,null)

        //actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(false)

        deleteBtn.setOnClickListener {
            val user = dbHelper.getTheUserByName(userName.toString())
            if(dbHelper.deleteTheNode(user,noteFromUser.toString())){
                Toast.makeText(this,"The note is deleted successfully",Toast.LENGTH_LONG).show()
                val intent = Intent(this,Inside::class.java)
                intent.putExtra(LoginPage.LOGIN_USER,user.userName)
                intent.putExtra(LoginPage.LOGIN_USER_MAIL,user.userMail)
                startActivity(intent)
            }
        }


        updateBtn.setOnClickListener {
            val user = dbHelper.getTheUserByName(userName.toString())
            val newNote : TextInputEditText = findViewById(R.id.setNoteEditText)
            if(dbHelper.updateTheNote(user,noteFromUser.toString(),newNote.text.toString())){
                Toast.makeText(this,"The note is updated successfully",Toast.LENGTH_LONG).show()
                val intent = Intent(this,Inside::class.java)
                intent.putExtra(LoginPage.LOGIN_USER,user.userName)
                intent.putExtra(LoginPage.LOGIN_USER_MAIL,user.userMail)
                /*
                val noteHolderText : TextView = findViewById(R.id.noteHolderText)
                noteHolderText.setText(newNote.toString())

                 */
                startActivity(intent)
            }else {
                Toast.makeText(this,"Update error",Toast.LENGTH_LONG).show()
            }
        }

        closeBtn.setOnClickListener {
            val intent = Intent(this,Inside::class.java)
            intent.putExtra(LoginPage.LOGIN_USER,userNameReal)
            //Toast.makeText(this,"Back button name $userNameReal",Toast.LENGTH_LONG).show()

            intent.putExtra(LoginPage.LOGIN_USER_MAIL,"")
            startActivity(intent)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()

         val intent = Intent(this,Inside::class.java)
        intent.putExtra(LoginPage.LOGIN_USER,userNameReal)
        Toast.makeText(this,"Back button name $userNameReal",Toast.LENGTH_LONG).show()

        intent.putExtra(LoginPage.LOGIN_USER_MAIL,"")
        startActivity(intent)

    }




}