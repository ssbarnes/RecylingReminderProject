package com.example.recyreminder.data

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.recyreminder.R
import java.util.*

class ResidentLogin : Activity() {

    private lateinit var login : Button
    private lateinit var user : EditText
    private lateinit var pass : EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.residents_login)

        login = findViewById(R.id.resLogin)
        login.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                user = findViewById(R.id.resUsername)
                pass = findViewById(R.id.resPassword)
                login(user.text.toString(), pass.text.toString())
            }
        })

    }

    fun login(username: String, password: String) {
        //TODO - Connect to firebase,
        // check if username and password exists in it, then switch activities
        Log.i("tag", username)
        Log.i("tag", password)
    }
}