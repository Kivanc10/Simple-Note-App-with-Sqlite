package com.kvnc.authwords

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.kvnc.authwords.R
import com.kvnc.authwords.model.DBOpenHelper
import com.kvnc.authwords.model.User

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        title = "Login Page"

        ifThereIsLoggedUser() // navigate if you already have logged in
        val message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE)
        val passwordFromRegister = intent.getStringExtra(MainActivity.USER_PASSWORD)

        val goBackToRegisterPage = findViewById<Button>(R.id.goBackToRegisterPage)


        setEditTexts(message,passwordFromRegister)

        //val logOutBtn : Button = findViewById(R.id.logOutBtn)

       // val deleteUSerBtn : Button = findViewById(R.id.deleteAccount)

        val loginButton : Button = findViewById(R.id.loginButtonId)


        goBackToRegisterPage.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra(JUST_ONE,"go_back")
            startActivity(intent)
            finish()
        }



        val dbHelper = DBOpenHelper(this,null)
/*
        logOutBtn.setOnClickListener {
            if(dbHelper.logOutForUser(message.toString())) {
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra(LOGOUT_MESSAGE,message)
                startActivity(intent)
                Toast.makeText(this,"The user is successfully logout",Toast.LENGTH_LONG).show()
            }
        }

        deleteUSerBtn.setOnClickListener {
                if(dbHelper.deleteTheUserAccount(message.toString())){
                    val intent = Intent(this,MainActivity::class.java)
                    intent.putExtra(LOGOUT_MESSAGE,message)
                    startActivity(intent)
                    Toast.makeText(this,"The user is successfully deleted",Toast.LENGTH_LONG).show()
                }
        }

 */


    }

    fun loginUser() : Boolean {

        val dbHelper = DBOpenHelper(this,null)

        val userNameEditText : TextInputEditText = findViewById(R.id.loginUserNameEditText)
        val userPasswordEditText : TextInputEditText = findViewById(R.id.loginUserPasswordEditText)

        val userName = userNameEditText.text.toString()
        val password = userPasswordEditText.text.toString()

        if(userName == null || userName == ""){
            return false
        }
        if(password == null || password == ""){
            return false
        }
 // TODO
        val user : User = dbHelper.getTheUserByName(userName)

        if(dbHelper.makeUserLoggedIn(user)){
            Toast.makeText(this,"User logged in to app successfully",Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(this,"An error occur during the logged in the app",Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun navigateToInside(view : View) {
        val userNameEditText : TextInputEditText = findViewById(R.id.loginUserNameEditText)
        val userPasswordEditText : TextInputEditText = findViewById(R.id.loginUserPasswordEditText)
        if(loginUser()){
            val context = view.context
            val userName = userNameEditText.text.toString()
            val password = userPasswordEditText.text.toString()
            val intent = Intent(this,Inside::class.java)
            intent.putExtra(LOGIN_USER,userName)
            intent.putExtra(LOGIN_USER_MAIL,password)
            context.startActivity(intent)
        }
    }


    fun setEditTexts(userName:String?,password:String?) {
        val userNameEditText : TextInputEditText = findViewById(R.id.loginUserNameEditText)
        val userPasswordEditText : TextInputEditText = findViewById(R.id.loginUserPasswordEditText)
        if (userName != null && password != null) {
            userNameEditText.setText(userName)
            userPasswordEditText.setText(password)
        }
    }

    fun ifThereIsLoggedUser() : Boolean {
        val dbHandler = DBOpenHelper(this,null)
        val myList : MutableList<User> = dbHandler.findLoggedLastUser()
        if(myList.size>0) {
            val message = myList[0].userName
            val mail = myList[0].userMail
            val intent = Intent(this,Inside::class.java)
            intent.putExtra(LOGIN_USER,message)
            intent.putExtra(LOGIN_USER_MAIL,mail)
            //finish() // to kill activity
            startActivity(intent)
            //this.finish()
            Toast.makeText(this,"navigate is successfully done",Toast.LENGTH_LONG).show()
            return true
        }else {
            Toast.makeText(this,"there is no user that registered",Toast.LENGTH_LONG).show()
            return false
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }



    companion object {
        const val JUST_ONE = "JUST_ONE"
        const val LOGIN_USER = "LOGIN_USER_NAME"
        const val LOGIN_USER_MAIL = "LOGIN_USER_MAIL"
    }
}