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
import kotlin.math.log

class PositionLogin : Activity() {
    // Lateinit variables
    private val database = Firebase.database.reference
    private lateinit var mPrefs: SharedPreferences
    private lateinit var position : String
    private lateinit var login : Button
    private lateinit var register : Button
    private lateinit var registerGc: Button
    private lateinit var user : EditText
    private lateinit var pass : EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPrefs = getSharedPreferences("appPrefs", Context.MODE_PRIVATE)

        setContentView(R.layout.residents_login)

        val positionVal = getIntent() // gets position val from select page
        position = positionVal.getStringExtra("position")!!

        //Login button
        login = findViewById(R.id.resLogin)
        login.setOnClickListener {
            user = findViewById(R.id.resUsername)
            pass = findViewById(R.id.resPassword)

            // Checks if values have been added
            val editTextArr = arrayOf(user, pass)
            var cont = true
            for (textElem in editTextArr) {
                val textVal = textElem.text.toString()
                if (textVal == "") {
                    textElem.error = "REQUIRED"
                    cont = false
                }
            }

            if (cont) {
                login(user.text.toString(), pass.text.toString())
            }
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

    // Checks if username and password exists in the position directory from firebase
    fun login(username: String, password: String) {
        Log.i("tag", username)
        Log.i("tag", password)

        val usersRef: DatabaseReference = database.child("users")
        val userRef = usersRef.child(position)
        var loginSuccess = false

        // Go through entire database checking for username and password
        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children) {
                    val name = user.child("username").value.toString()
                    val passW = user.child("password").value.toString()

                    if (name == username && passW == password) {
                        loginSuccess = true

                        if (position == "residents") {
                            val addr = user.child("address").value.toString()
                            val editor = mPrefs.edit()
                            editor.putString("address", addr)
                            editor.commit()
                        }

                        break
                    }
                }

                // Opens appropriate position menu
                if (loginSuccess) {
                    if (position == "residents") {
                        val notificationIntent = Intent(this@PositionLogin, ResidentNotifications::class.java)
                        notificationIntent.putExtra("username", username)
                        startActivity(notificationIntent)
                    } else {
                        val reportingIntent = Intent(this@PositionLogin, GCReportViolation::class.java)
                        reportingIntent.putExtra("username", username)
                        startActivity(reportingIntent)
                    }
                } else {
                    pass.setText("")
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