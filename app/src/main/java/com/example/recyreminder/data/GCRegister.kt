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

    // Lateinit variables to be used
    private lateinit var register : Button
    private lateinit var user : EditText
    private lateinit var pass : EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gc_register)

        Log.i(TAG, "Entered GC Registration")

        // register button for registering accoutn
        register = findViewById(R.id.gcRegRegister)
        register.setOnClickListener {
            user = findViewById(R.id.gcRegUsername)
            pass = findViewById(R.id.gcRegPassword)

            // Validates information given in text fields (checks if they are empty)
            val editTextArr = arrayOf(user, pass)
            var cont = true
            for (textElem in editTextArr) {
                val textVal = textElem.text.toString()
                if (textVal == "") {
                    textElem.error = "REQUIRED"
                    cont = false
                }
            }

            // Only if entries are not empty, will continue to register account
            if (cont) {
                sendData(user.text.toString(), pass.text.toString())
            }

        }

    }

    // Send data to firebase to check validity
    fun sendData(username: String, password: String) {
        // Creates new entry of account
        val newUser: MutableMap<String, Any> = HashMap()
        newUser["username"] = username
        newUser["password"] = password

        val database = Firebase.database.reference
        val usersRef: DatabaseReference = database.child("users")
        val userRef = usersRef.child("collectors/" + username)

        // Check if user already exists
        usersRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var cont = true
                for (userType in snapshot.children) {
                    for (userN in userType.children) {
                        val name = userN.child("username").value.toString()

                        if(name == username) {
                            cont = false
                            Toast.makeText(applicationContext, "Username already exists", Toast.LENGTH_SHORT).show()
                            Log.i(TAG, "Username already exists")
                            user.setText("")
                            pass.setText("")
                            break
                        }
                    }

                    if (!cont) { break } // User already exists, will not register 
                }

                // If user exists, complete registration
                if (cont) {
                    userRef.setValue(newUser)
                    Log.i(TAG, "Registration complete")
                    Toast.makeText(applicationContext, "Registration complete", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i(ResidentRegister.TAG, error.message)
            }
        })

    }

    companion object {
        const val TAG = "Recycling Reminder"
    }
}