package com.kvnc.authwords

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.kvnc.authwords.model.DBOpenHelper
import com.kvnc.authwords.model.User

class CRUDNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_note)

        val userName = intent.getStringExtra(Inside.LOGIN_USER)
        val dbHelper = DBOpenHelper(this,null)

        val user : User = dbHelper.getTheUserByName(userName.toString())

        val editNoteText : TextInputEditText = findViewById(R.id.insideEditText)


        val saveNote : Button = findViewById(R.id.saveNoteBtn) // to save notes via btn

        saveNote.setOnClickListener {
            val editNoteStr = editNoteText.text.toString()
            if(editNoteStr == null || editNoteStr == "") {
                Toast.makeText(this,"Your text is not proper to save please arrange your note",Toast.LENGTH_LONG).show()
            }
            dbHelper.addNote(user,editNoteStr) // to add note to db
            val tempCursor : Cursor? = dbHelper.getAllNotes(user)
            if(tempCursor != null && tempCursor.moveToFirst()) {
                Toast.makeText(this,"Your note is added to db successfully",Toast.LENGTH_LONG).show()
                editNoteText.setText("")
                val intent = Intent(this,Inside::class.java) // go back to inside
                intent.putExtra(Inside.LOGIN_USER,userName)
                intent.putExtra(Inside.LOGIN_USER_MAIL,user.userMail)
                startActivity(intent)
            }else {
                Toast.makeText(this,"An error occurred during the add your note to db",Toast.LENGTH_LONG).show()
            }
        }

    }
}