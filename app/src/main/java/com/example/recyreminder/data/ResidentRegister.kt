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

class ResidentRegister : Activity() {

    private lateinit var register : Button
    private lateinit var user : EditText
    private lateinit var pass : EditText
    private lateinit var address : EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.residents_register)

        register = findViewById(R.id.resRegRegister)
        register.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                user = findViewById(R.id.resRegUsername)
                pass = findViewById(R.id.resRegPassword)
                address = findViewById(R.id.resRegAddress)

                sendData(user.text.toString(), pass.text.toString(), address.text.toString())
            }
        })

    }

    fun sendData(username: String, password: String, addr: String) {
        //TODO - post data to firebase database
        Log.i("tag", username)
        Log.i("tag", password)
        Log.i("tag", addr)
    }
}