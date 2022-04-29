package com.example.recyreminder.data

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.recyreminder.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class PositionLogin : Activity() {

    private lateinit var login : Button
    private lateinit var register : Button
    private lateinit var registerGc: Button
    private lateinit var user : EditText
    private lateinit var pass : EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.residents_login)

        login = findViewById(R.id.resLogin)
        login.setOnClickListener {
            user = findViewById(R.id.resUsername)
            pass = findViewById(R.id.resPassword)
            login(user.text.toString(), pass.text.toString())
        }

        register = findViewById(R.id.resRegister)
        register.setOnClickListener {
            val registerIntent = Intent(this@PositionLogin, ResidentRegister::class.java)

            startActivity(registerIntent)
        }

        registerGc = findViewById(R.id.gcRegister)
        registerGc.setOnClickListener {
//            val registerIntent = Intent(this@PositionLogin, ResidentRegister::class.java)
//
//            startActivity(registerIntent)
        }
    }

    fun login(username: String, password: String) {
        //TODO - Connect to firebase,
        // check if username and password exists in it, then switch activities

        Log.i("tag", username)
        Log.i("tag", password)

        val database = Firebase.database.reference
        val usersRef: DatabaseReference = database.child("users")
        val userRef = usersRef.child(username)

        val newUser: MutableMap<String, Any> = HashMap()
        newUser["username"] = username
        newUser["password"] = password

        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // TODO - check password and switch to appropriate interface
                    Log.i("tag", "Yay it already exists")
                } else {
                    // TODO - reject login attempt if user doesn't exist
                    Log.i("tag", "Nope")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("tag", error.message)
            }

        })
    }
}