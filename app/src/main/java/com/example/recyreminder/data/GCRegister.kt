package com.example.recyreminder.data

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.recyreminder.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class GCRegister: Activity() {

    private lateinit var register : Button
    private lateinit var user : EditText
    private lateinit var pass : EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gc_register)

        Log.i(TAG, "Entered GC Registration")

        register = findViewById(R.id.gcRegRegister)
        register.setOnClickListener {
            user = findViewById(R.id.gcRegUsername)
            pass = findViewById(R.id.gcRegPassword)

            sendData(user.text.toString(), pass.text.toString())
            finish()
        }

    }

    fun sendData(username: String, password: String) {
        //TODO - post data to firebase database
//        Log.i(TAG, username)
//        Log.i(TAG, password)

        val newUser: MutableMap<String, Any> = HashMap()
        newUser["username"] = username
        newUser["password"] = password
        newUser["position"] = "gc"

        val database = Firebase.database.reference
        val usersRef: DatabaseReference = database.child("users/collectors")
        val userRef = usersRef.child(username)

        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // TODO - tell user they're already registered
                    Log.i(ResidentRegister.TAG, "Uh oh username already exists")
                    Toast.makeText(applicationContext, "Username already exists", Toast.LENGTH_SHORT).show()
                } else {
                    Log.i(ResidentRegister.TAG, "Come on in")
                    userRef.setValue(newUser)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i(ResidentRegister.TAG, error.message)
            }
        })

        Log.i(TAG, "Completed GC Registration")
    }

    companion object {
        const val TAG = "Recycling Reminder"
    }
}