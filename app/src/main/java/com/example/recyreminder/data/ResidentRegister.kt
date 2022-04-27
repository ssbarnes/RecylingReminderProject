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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
        register.setOnClickListener {
            user = findViewById(R.id.resRegUsername)
            pass = findViewById(R.id.resRegPassword)
            address = findViewById(R.id.resRegAddress)

            sendData(user.text.toString(), pass.text.toString(), address.text.toString())
        }

    }

    fun sendData(username: String, password: String, addr: String) {
        //TODO - post data to firebase database
        Log.i(TAG, username)
        Log.i(TAG, password)
        Log.i(TAG, addr)

        val newUser: MutableMap<String, Any> = HashMap()
        newUser["username"] = username
        newUser["password"] = password
        newUser["address"] = addr

        val database = Firebase.database.reference
        val usersRef: DatabaseReference = database.child("users")
        val userRef = usersRef.child(username)

        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // TODO - tell user they're already registered
                    Log.i(TAG, "Uh oh username already exists")
                } else {
                    Log.i(TAG, "Come on in")
                    userRef.setValue(newUser)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, error.message)
            }
        })
    }

    companion object {
        const val TAG = "Recycling Reminder"
    }
}