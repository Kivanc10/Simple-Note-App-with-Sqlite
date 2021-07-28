package com.kvnc.authwords.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.TextView
import kotlin.math.log

class DBOpenHelper(context: Context,factory: SQLiteDatabase.CursorFactory?):SQLiteOpenHelper(context,DATABASE_NAME,factory,DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = (
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME1 + " TEXT," +
                        COLUMN_NAME2 + " TEXT," +
                        COLUMN_NAME3 + " TEXT," +
                        COLUMN_NAME4 + " INTEGER," +
                        COLUMN_REGISTER + " INTEGER" + ")"
                )

        val CREATE_PRODUCTS_TABLE2 = (
                "CREATE TABLE " + TABLE_NAME2 + "(" +
                        COLUMN_CONTENT + " TEXT," +
                        NAME_USER_REF + " TEXT," +
                    "FOREIGN KEY($NAME_USER_REF) REFERENCES $TABLE_NAME($COLUMN_NAME1)" + ")")
                db.execSQL(CREATE_PRODUCTS_TABLE)
                db.execSQL(CREATE_PRODUCTS_TABLE2)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2) // i added
        onCreate(db)
    }

    fun toPreventDuplicate(userName : String) : Cursor? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME1 LIKE ?"
        return db.rawQuery(query, arrayOf(userName))
    }

    fun logOutForUser(userName : String) : Boolean {
        val db = this.writableDatabase

        val values = ContentValues().apply { // make the user unregistered
            put(COLUMN_NAME4,0)
        }

        val selection = "$COLUMN_NAME1 LIKE ?"
        val selectionArgs = arrayOf(userName)

        val count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
        )

        return count != 0
    }

    fun getTheAllNotesBelongToUser(userName: String,statueTextView: TextView) : MutableList<String> {
        val user : User = this.getTheUserByName(userName)
        val noteList : MutableList<String> = mutableListOf()
        val cursor : Cursor? = this.getAllNotes(user)
        if(cursor != null && cursor.moveToFirst()) {
            var tempNote = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_CONTENT))
            noteList.add(tempNote)
            while (cursor.moveToNext()){
                tempNote = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_CONTENT))
                noteList.add(tempNote)
            }
            cursor.close()
        }else{
            statueTextView.text = "There is no note to show belong to the user $userName"
        }
        noteList.reverse()
        return noteList
    }

/*
    fun loginUser(userName: String,password : String) : Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME4,1)
        }
        val selection = "$COLUMN_NAME1 LIKE ? and $COLUMN_NAME3 LIKE ?"
        val selectionArgs = arrayOf(userName,password)

        val count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
        )
        return count != 0
    }

 */

    fun deleteTheUserAccount(userName: String) : Boolean {
        val db = this.writableDatabase


        val selection = "$COLUMN_NAME1 LIKE ?"
        val selectionArgs = arrayOf(userName)

        val count = db.delete(TABLE_NAME,selection,selectionArgs)

        return count != 0
    }

    fun deleteTheNode(user: User,oldNote: String) : Boolean {
        val db = this.writableDatabase
        val selection = "$COLUMN_CONTENT LIKE ? and $NAME_USER_REF LIKE ?"
        val selectionArgs = arrayOf(oldNote,user.userName)
        val count = db.delete(TABLE_NAME2,selection,selectionArgs)
        return count != 0
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put(COLUMN_NAME1,user.userName)
        values.put(COLUMN_NAME2,user.userMail)
        values.put(COLUMN_NAME3,user.password)
        values.put(COLUMN_NAME4,user.loggedIn)
        values.put(COLUMN_REGISTER,user.register)
        val db = this.writableDatabase
        db.insert(TABLE_NAME,null,values)
        db.close()
    }

    fun addNote(user:User,note : String) {
        val values = ContentValues()
        values.put(COLUMN_CONTENT,note)
        values.put(NAME_USER_REF,user.userName)
        val db = this.writableDatabase
        db.insert(TABLE_NAME2,null,values)
        db.close()
    }
/*
    fun isRegistered(username : String) : Boolean {
        val db = this.readableDatabase
        val done : Int = 1
        val myCursor : Cursor? = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME1 = $username and $COLUMN_REGISTER = $done", null)
        if (myCursor == null)
            return false
        return true

    }
*/
    fun findLoggedLastUser() : MutableList<User> {
        val userList = mutableListOf<User>()
        val db = this.readableDatabase
        val done : Int = 1
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME4 = ? ORDER BY $COLUMN_ID"
        val result = db.rawQuery(query, arrayOf(done.toString()))
        if(result.moveToFirst()) {
            do {
                val user = User()
                user.id = result.getString(result.getColumnIndex(COLUMN_ID)).toInt()
                user.userName = result.getString(result.getColumnIndex(COLUMN_NAME1))
                user.userMail = result.getString(result.getColumnIndex(COLUMN_NAME2))
                user.password =  result.getString(result.getColumnIndex(COLUMN_NAME3))
                user.loggedIn = result.getString(result.getColumnIndex(COLUMN_NAME4)).toInt()
                user.register = result.getString(result.getColumnIndex(COLUMN_REGISTER)).toInt()
                userList.add(user)
            }while (result.moveToNext())
        }

    userList.reverse()
        result.close()
        db.close()
        return userList
    }

    fun getTheUserByName(userName: String) : User {
        val user = User()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME1 LIKE ?"
        val result = db.rawQuery(query, arrayOf(userName))
        if (result.moveToFirst()){
            do {
                user.id = result.getString(result.getColumnIndex(COLUMN_ID)).toInt()
                user.userName = result.getString(result.getColumnIndex(COLUMN_NAME1))
                user.userMail = result.getString(result.getColumnIndex(COLUMN_NAME2))
                user.password =  result.getString(result.getColumnIndex(COLUMN_NAME3))
                user.loggedIn = result.getString(result.getColumnIndex(COLUMN_NAME4)).toInt()
                user.register = result.getString(result.getColumnIndex(COLUMN_REGISTER)).toInt()
            }while (result.moveToNext())
        }
        return user
    }

    fun findRegisteredLastUser() : MutableList<User> {
        val userList = mutableListOf<User>()
        val db = this.readableDatabase
        val done : Int = 1
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_REGISTER=?"
        val result = db.rawQuery(query, arrayOf(done.toString()))
        if(result.moveToFirst()){
            do {
                val user = User()
                user.id = result.getString(result.getColumnIndex(COLUMN_ID)).toInt()
                user.userName = result.getString(result.getColumnIndex(COLUMN_NAME1))
                user.userMail = result.getString(result.getColumnIndex(COLUMN_NAME2))
                user.password =  result.getString(result.getColumnIndex(COLUMN_NAME3))
                user.loggedIn = result.getString(result.getColumnIndex(COLUMN_NAME4)).toInt()
                user.register = result.getString(result.getColumnIndex(COLUMN_REGISTER)).toInt()
                userList.add(user)
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return userList
    }
    fun getLastLoggedUser() : User {
        val db = this.readableDatabase
        val done : Int = 1
        val query = "SELECT * FROM $TABLE_NAME WHERE COLUMN_NAME4 =?"
        val result = db.rawQuery(query, arrayOf(done.toString()))
        val user = User()
        if(result.moveToFirst()){
            do {
                user.id = result.getString(result.getColumnIndex(COLUMN_ID)).toInt()
                user.userName = result.getString(result.getColumnIndex(COLUMN_NAME1))
                user.userMail = result.getString(result.getColumnIndex(COLUMN_NAME2))
                user.password =  result.getString(result.getColumnIndex(COLUMN_NAME3))
                user.loggedIn = result.getString(result.getColumnIndex(COLUMN_NAME4)).toInt()
                user.register = result.getString(result.getColumnIndex(COLUMN_REGISTER)).toInt()
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return user
    }


    fun makeUserLoggedIn(user : User) : Boolean {
        user.loggedIn = 1
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME4,1)
        }

        val selection = "$COLUMN_NAME1 LIKE ?"
        val selectionArgs = arrayOf("${user.userName}")

        val count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
        )

        return count != 0
    }

    fun updateTheNote(user: User,oldNote : String,newNote : String) : Boolean{
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CONTENT,newNote)
        }

        val selection = "$COLUMN_CONTENT LIKE ? and $NAME_USER_REF LIKE ?"
        val selectionArgs = arrayOf(oldNote,user.userName)

        val count = db.update(TABLE_NAME2,values,selection,selectionArgs)

        return count != 0
    }


    fun makeUserRegistered(user: User) : Boolean{
        user.register = 1
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_REGISTER,user.register)
        }

        val selection = "$COLUMN_NAME1 LIKE ?"
        val selectionArgs = arrayOf("${user.userName}")

        val count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
        )

        return count != 0
    }
/*
    fun getAllNotes(user: User) : Cursor? {
        val userId : Int = user.id
        val db = this.readableDatabase
        return db.rawQuery("SELECT user.username,note.content FROM $TABLE_NAME INNER JOIN $TABLE_NAME2 ON note.user_id = user._id",null)
    }


    fun getLastLoggedInUser() : Cursor? { // is not null
        val db = this.readableDatabase
        return db.rawQuery("SELECT * from $TABLE_NAME WHERE $COLUMN_NAME4 = 1",null)
    }
*/
    /*
    fun dropTable() : Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("DROP TABLE $TABLE_NAME",null)
    }
    */


    fun getAllUser() : Cursor? {
        val db = this.readableDatabase
        val done : Int = 1
        return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID",null)
    }

    fun getAllNotes(user: User) : Cursor? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME2 WHERE $NAME_USER_REF LIKE ?"
        return db.rawQuery(query, arrayOf(user.userName.toString()))
    }



    /*
    fun deleteAllRows() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME,null,null)
    }

     */

    companion object {
        private val DATABASE_VERSION  = 1
        private val DATABASE_NAME = "userStore.db"
        val TABLE_NAME = "user"
        val TABLE_NAME2 = "note"
        val COLUMN_ID = "_id"
        val ID_USER = "user_id"
        val NAME_USER_REF = "user_name_ref"
        val COLUMN_NAME1 = "username"
        val COLUMN_NAME2 = "usermail"
        val COLUMN_NAME3 = "userpassword"
        val COLUMN_REGISTER = "register"
        val COLUMN_NAME4 = "loggedIn"
        val COLUMN_CONTENT = "content"
    }

}