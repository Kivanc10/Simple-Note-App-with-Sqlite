package com.kvnc.authwords

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.kvnc.authwords.databinding.ActivityMainBinding
import com.kvnc.authwords.model.DBOpenHelper
import com.kvnc.authwords.model.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val dbHandler = DBOpenHelper(this,null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        title = "Register Page"
        setContentView(binding.root)
//
        val go_back_border = intent.getStringExtra(LoginPage.JUST_ONE)

        val myList : MutableList<User> =  dbHandler.findRegisteredLastUser()

        if (myList.size > 0 && go_back_border == null) {
            val message = myList[0].userName
            val pswrd = myList[0].password
            val intent = Intent(this,LoginPage::class.java)
            intent.putExtra(EXTRA_MESSAGE,message)
            intent.putExtra(USER_PASSWORD,pswrd)
            startActivity(intent)
            finish()
            Toast.makeText(this,"navigate is successfully done",Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(this,"there is no user that registered",Toast.LENGTH_LONG).show()
        }

        //
       //dbHandler.deleteAllRows() // one time
        /*
        val tempBtn : Button = binding.tempBtn
        val dbRes : TextView = binding.showUsers
        tempBtn.setOnClickListener {
            dbRes.text = ""
            val cursor : Cursor? = dbHandler.getAllUser()
            if (cursor != null && cursor.moveToFirst()) {
               //cursor!!.moveToFirst()
                dbRes.append((cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME1))))
                dbRes.append("reg\t")
                dbRes.append((cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_REGISTER))))
                dbRes.append("id\t")
                dbRes.append((cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_ID))))
                dbRes.append("\n")
                while (cursor.moveToNext()) {
                    dbRes.append((cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME1))))
                    dbRes.append("reg\t")
                    dbRes.append((cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_REGISTER))))
                    dbRes.append("id\t")
                    dbRes.append((cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_ID))))
                    dbRes.append("\n")
                }
                cursor.close()
            }else {
                dbRes.append("There is no user to show")
            }
        }
        */

    }

    private fun registerUser() : Boolean {

        val userNameText : TextInputEditText = binding.getUserNameText
        val userMailText : TextInputEditText = binding.getMailText
        val userPasswordText : TextInputEditText = binding.getUserPasswordText
        //val registerBtn : Button = binding.registerBtn

        val userName = userNameText.text.toString()
        val userMail = userMailText.text.toString()
        val userPassword = userPasswordText.text.toString()

        /*
        if(dbHandler.isRegistered(userName)) {
            Toast.makeText(this,"This user name has been used already",Toast.LENGTH_LONG).show()
        }
        */


            val user = User(userName,userMail,userPassword,0,0)
            //val dbHandler = DBOpenHelper(this,null)

            if (userName == "" || userName == null || userName.length < 4) {
                Toast.makeText(this,"Your user name is inconvenient please arrange it again",Toast.LENGTH_LONG).show()
                return false
            }
            if (userMail == "" || userMail == null || !userMail.contains("@") ) {
                Toast.makeText(this,"Your mail is inconvenient please arrange it again",Toast.LENGTH_LONG).show()
                return false
            }
            if (userPassword == "" || userPassword == null || userPassword.equals("password",ignoreCase = true) || userPassword.length < 5){
                Toast.makeText(this,"Your password is inconvenient please arrange it again",Toast.LENGTH_LONG).show()
                return false
            }
           val preventCursor = dbHandler.toPreventDuplicate(userName)
        if (preventCursor != null) {
            if(preventCursor.moveToFirst()){
                Toast.makeText(this,"This user name has been used already",Toast.LENGTH_LONG).show()
                return true // false --> true
            }
        }
            dbHandler.addUser(user)
            if(dbHandler.makeUserRegistered(user)){
                Toast.makeText(this,"User is added to db successfully",Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(this,"An error occurred during the added to db successfully",Toast.LENGTH_LONG).show()
            }
    return true
    }

    fun sendMessage(view : View) {
        val userNameText : TextInputEditText = binding.getUserNameText
        val userPasswordText : TextInputEditText = binding.getUserPasswordText

        if (registerUser()) {
            val context = view.context
            val message = userNameText.text.toString()
            val pswrd_msg = userPasswordText.text.toString()
            val intent = Intent(this,LoginPage::class.java)
            intent.putExtra(EXTRA_MESSAGE,message)
            intent.putExtra(USER_PASSWORD,pswrd_msg)
            context.startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_MESSAGE = "MESSAGE"
        const val USER_PASSWORD = "USER_PASSWORD_MSG"
    }
}