package com.example.recyreminder.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class PositionLogin : Activity() {
    private val database = Firebase.database.reference
    private lateinit var mPrefs: SharedPreferences

    private lateinit var login : Button
    private lateinit var register : Button
    private lateinit var registerGc: Button
    private lateinit var user : EditText
    private lateinit var pass : EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPrefs = getSharedPreferences("appPrefs", Context.MODE_PRIVATE)

        setContentView(R.layout.residents_login)


        //Login button
        login = findViewById(R.id.resLogin)
        login.setOnClickListener {
            user = findViewById(R.id.resUsername)
            pass = findViewById(R.id.resPassword)
            login(user.text.toString(), pass.text.toString())
            //Move this into else once we can get type of
            //user from firebase
            //Go to notifications tab
//            val notificationIntent = Intent(this@PositionLogin, ResidentNotifications::class.java)
//            startActivity(notificationIntent)

//             val mapsIntent = Intent(this@PositionLogin, GCMap::class.java)
//             startActivity(mapsIntent)


        }

        //Resident Register
        register = findViewById(R.id.resRegister)
        register.setOnClickListener {
            val registerIntent = Intent(this@PositionLogin, ResidentRegister::class.java)

            startActivity(registerIntent)
        }

        //Garbage Collector Register
        registerGc = findViewById(R.id.gcRegister)
        registerGc.setOnClickListener {
            val registerIntent = Intent(this@PositionLogin, GCRegister::class.java)

            startActivity(registerIntent)
        }
    }

    fun login(username: String, password: String) {
        Log.i("tag", username)
        Log.i("tag", password)

        val usersRef: DatabaseReference = database.child("users")
        var loginSuccess = false

        // Go through entire database checking for username and password
        usersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userType in snapshot.children) {
                    for (user in userType.children) {
                        val name = user.child("username").value.toString()
                        val pass = user.child("password").value.toString()
                        val addr = user.child("address").value.toString()

                        // If username and password match for given user
                        if(username == name && password == pass) {
                            Log.i(TAG, addr)

                            loginSuccess = true

                            // Save address to app preferences
                            val editor = mPrefs.edit()
                            editor.putString("address", addr)
                            editor.commit()

                            // Switch to appropriate interface
                            if(userType.key == "residents") {
                                val notificationIntent = Intent(this@PositionLogin, ResidentNotifications::class.java)
                                startActivity(notificationIntent)
                            } else if(userType.key == "collectors") {
                                // TODO - switch to garbage collectors interface
                                val reportingIntent = Intent(this@PositionLogin, GCReportViolation::class.java)
                                startActivity(reportingIntent)
                            }
                            break
                        }
                    }
                }
                // Reject login attempt if match not found
                if(!loginSuccess) {
                    Toast.makeText(
                        this@PositionLogin,
                        "Login failed",
                        Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("tag", error.message)
            }

        })
    }

    companion object {
        const val TAG = "Recycling Reminder"
    }
}