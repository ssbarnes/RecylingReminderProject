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
import android.widget.Toast
import com.example.recyreminder.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class ResidentRegister : Activity() {

    private lateinit var register : Button
    private lateinit var user : EditText
    private lateinit var pass : EditText
    private lateinit var address : EditText
    private lateinit var city : EditText
    private lateinit var countryState : EditText
    private lateinit var zipCode : EditText

    private val database: DatabaseReference = Firebase.database.reference
    private val usersRef: DatabaseReference = database.child("users")

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.residents_register)

        Log.i(TAG, "Entered Resident Registration")

        register = findViewById(R.id.resRegRegister)
        register.setOnClickListener {
            user = findViewById(R.id.resRegUsername)
            pass = findViewById(R.id.resRegPassword)
            address = findViewById(R.id.resRegAddress)
            city = findViewById(R.id.resRegCity)
            countryState = findViewById(R.id.resRegCountryState)
            zipCode = findViewById(R.id.resRegZipCode)


            Log.i(TAG, user.text.toString())
            Log.i(TAG, pass.text.toString())
            Log.i(TAG, address.text.toString())

//            val newUser: MutableMap<String, Any> = HashMap()
//            newUser["username"] = user.text.toString()
//            newUser["password"] = pass.text.toString()
//            newUser["address"] = address.text.toString()
//
//            sendData(newUser)

            val newAddress = address.text.toString() + ", " + city.text.toString() + ", " +
                    countryState.text.toString() + " " + zipCode.text.toString()

            sendData(user.text.toString(), pass.text.toString(), newAddress)
            finish()
        }

        /* TODO - This is just a proof of concept. Move elsewhere as needed.
            This is an example of the app responding to the password for a user
            being changed in the database
        */
        val seifPassRef = usersRef.child("seif/password")

        // Check if password for "seif" has changed
        seifPassRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<String>()
                Toast.makeText(applicationContext,
                    "Password is now $value",
                    Toast.LENGTH_LONG).show()
                Log.d(TAG, "Password is now $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun sendData(username: String, password: String, addr: String) {
        val newUser: MutableMap<String, Any> = HashMap()
        newUser["username"] = username
        newUser["password"] = password
        newUser["address"] = addr
        newUser["position"] = "resident"

        val database = Firebase.database.reference
        val usersRef: DatabaseReference = database.child("users")
        val userRef = usersRef.child(addr)

        // Check if user already exists
        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // TODO - tell user they're already registered
                    Log.i(TAG, "Uh oh username already exists")
                    Toast.makeText(applicationContext, "Username already exists", Toast.LENGTH_SHORT)
                } else {
                    Log.i(TAG, "Come on in")
                    userRef.setValue(newUser)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, error.message)
            }
        })

        Log.i(TAG, "Completed Resident Registration")
    }

    companion object {
        const val TAG = "Recycling Reminder"
    }
}