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

        // Text fields for registering account
        register = findViewById(R.id.resRegRegister)
        register.setOnClickListener {
            user = findViewById(R.id.resRegUsername)
            pass = findViewById(R.id.resRegPassword)
            address = findViewById(R.id.resRegAddress)
            city = findViewById(R.id.resRegCity)
            countryState = findViewById(R.id.resRegCountryState)
            zipCode = findViewById(R.id.resRegZipCode)

            // Checks if fields are not empty
            val editTextArr = arrayOf(user, pass, address, city, countryState, zipCode)
            var cont = true
            for (textElem in editTextArr) {
                val textVal = textElem.text.toString()
                if (textVal == "") {
                    textElem.error = "REQUIRED"
                    cont = false
                }
            }

            if (cont) {
                val newAddress = address.text.toString() + ", " + city.text.toString() + ", " +
                        countryState.text.toString() + " " + zipCode.text.toString()

                sendData(user.text.toString(), pass.text.toString(), newAddress)
            }
        }

    }

    // Send data to firebase and create account
    private fun sendData(username: String, password: String, addr: String) {
        val newUser: MutableMap<String, Any> = HashMap()
        newUser["username"] = username
        newUser["password"] = password
        newUser["address"] = addr

        val database = Firebase.database.reference
        val usersRef: DatabaseReference = database.child("users")
        val userRef = usersRef.child("residents/" + addr)

        // Check if user and address already exists
        usersRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var cont = true
                var message = ""
                for (userType in snapshot.children) {
                    for (userN in userType.children) {
                        val name = userN.child("username").value.toString()
                        val uAddr = userN.child("address").value.toString()

                        if (name == username) {
                            cont = false
                            message += "Username already exists\n"
                            user.setText("")
                            pass.setText("")
                        }
                        if (uAddr == addr) {
                            cont = false
                            message += "Address already exists"
                            address.setText("")
                            city.setText("")
                            countryState.setText("")
                            zipCode.setText("")
                        }
                        if (!cont) {
                            Log.i(TAG, message)
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                            break
                        }
                    }

                    if (!cont) { break }
                }

                if (cont) {
                    userRef.setValue(newUser)
                    Log.i(TAG, "Registration complete")
                    Toast.makeText(applicationContext, "Registration complete", Toast.LENGTH_SHORT).show()
                    finish()
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