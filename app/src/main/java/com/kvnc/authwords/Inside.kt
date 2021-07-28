package com.kvnc.authwords

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.kvnc.authwords.adapter.UserNoteAdapter
import com.kvnc.authwords.model.DBOpenHelper
import com.kvnc.authwords.model.User

class Inside : AppCompatActivity() {
    private lateinit var userName : String
    private lateinit var userPassword : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside)
        val dbHandler = DBOpenHelper(this,null)
    //getLastLoggedUser
        //val userName : String?
        if(intent.getStringExtra(LoginPage.LOGIN_USER) != null) {
            userName = intent.getStringExtra(LoginPage.LOGIN_USER)!!
        }else{
            val lastUser = dbHandler.getLastLoggedUser()
            userName = lastUser.userName.toString()
        }
        val email = intent.getStringExtra(LoginPage.LOGIN_USER_MAIL)
        val textView : TextView = findViewById(R.id.loginWelcomeText)
        val recyclerView : RecyclerView = findViewById(R.id.recyclerViewInside)
        title = "My Notes"
        textView.setText("Welcome $userName")





        val statueOfNotes : TextView = findViewById(R.id.showStatueOfNotes)
        val newNoteBtn : Button = findViewById(R.id.newNoteBtn)



        newNoteBtn.setOnClickListener {
            val intent = Intent(this,CRUDNote::class.java) // send the user
            intent.putExtra(LOGIN_USER,userName)
            intent.putExtra(LOGIN_USER_MAIL,email)
            startActivity(intent)
        }

        val user = dbHandler.getTheUserByName(userName.toString())
        userPassword = user.password.toString()
        getUserNotesByAdapter(userName.toString(),dbHandler,recyclerView,statueOfNotes,user)




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       val id = item.getItemId()
        val dbHelper = DBOpenHelper(this,null)
        if(id == R.id.logoutUser) {

            if(dbHelper.logOutForUser(userName)) {
                val intent = Intent(this,MainActivity::class.java)
                //intent.putExtra(LoginPage.LOGOUT_MESSAGE,userName)
                intent.putExtra(MainActivity.EXTRA_MESSAGE,userName)
                intent.putExtra(MainActivity.USER_PASSWORD,userPassword)
                startActivity(intent)
                Toast.makeText(this,"The user is successfully logout",Toast.LENGTH_LONG).show()
            }

            return true
        }
        if(id == R.id.deleteTheUser) {
            if(dbHelper.deleteTheUserAccount(userName)){
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_MESSAGE,userName)
                intent.putExtra(MainActivity.USER_PASSWORD,userPassword)
                startActivity(intent)
                Toast.makeText(this,"The user is successfully deleted",Toast.LENGTH_LONG).show()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getUserNotesByAdapter(userName : String,dbHandler : DBOpenHelper,recyclerView: RecyclerView,textView: TextView,user : User) {
        var allNotes : MutableList<String> = mutableListOf()
        allNotes  = dbHandler.getTheAllNotesBelongToUser(userName,textView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserNoteAdapter(this,allNotes,user)
    }

    override fun onBackPressed() {
        Toast.makeText(this,"There is no back action",Toast.LENGTH_LONG).show()
    }

    companion object {
        const val LOGIN_USER = "LOGIN_USER_NAME"
        const val LOGIN_USER_MAIL = "LOGIN_USER_MAIL"
        const val NOTE = "note"
        const val USER_NAME = "user_name"
    }
}