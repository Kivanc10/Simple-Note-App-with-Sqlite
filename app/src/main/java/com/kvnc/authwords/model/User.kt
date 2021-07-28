package com.kvnc.authwords.model

class User {
    var id : Int = 0
    var userName : String? = null
    var userMail : String? = null
    var password : String? = null
    var loggedIn : Int = 0
    var register : Int = 0

    constructor(userName : String,userMail : String,password : String,loggedIn : Int,register : Int) {
        this.userName = userName;
        this.userMail = userMail;
        this.password = password;
        this.loggedIn = loggedIn
        this.register = register
    }

    constructor() {

    }

}