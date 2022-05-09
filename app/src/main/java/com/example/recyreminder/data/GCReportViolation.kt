package com.example.recyreminder.data

import android.util.Log
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.recyreminder.R
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class GCReportViolation: Activity() {
    // Lateinit variables to use later
    private lateinit var address: EditText
    private lateinit var city: EditText
    private lateinit var countryState: EditText
    private lateinit var zipCode: EditText
    private lateinit var violation: EditText
    private lateinit var report : Button
    private lateinit var viewMap : Button
    private lateinit var logout : Button
    private lateinit var unregister : Button
    private lateinit var username : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gc_menu)

        Log.i(TAG, "Entered GC Menu/Reporting Violation")

        // Text fields are initialized
        address = findViewById(R.id.address)
        city = findViewById(R.id.city)
        countryState = findViewById(R.id.countryState)
        zipCode = findViewById(R.id.zipCode)

        // Button for reporting violation
        report = findViewById(R.id.report)
        report.setOnClickListener {
            violation = findViewById(R.id.violation)

            // Checks if text fields have been entered properly
            val editTextArr = arrayOf(address, city, countryState, zipCode, violation)
            var cont = true
            for (textElem in editTextArr) {
                val textVal = textElem.text.toString()
                if (textVal == "") {
                    textElem.error = "REQUIRED"
                    cont = false
                }
            }

            if (cont) {
                // Formats address
                val addr = address.text.toString() + ", " + city.text.toString() + ", " +
                        countryState.text.toString() + " " + zipCode.text.toString()

                reportViolation(addr, violation.text.toString()) // reports address with violation
            }

        }

        // View map button
        viewMap = findViewById(R.id.map)
        viewMap.setOnClickListener {
            val mapIntent = Intent(
                this@GCReportViolation,
                GCMap::class.java
            ) // starts GCMap activity with result expected

            startActivityForResult(mapIntent, 1)
        }

        //Logout button
        logout = findViewById(R.id.logout)
        logout.setOnClickListener {
            Toast.makeText(applicationContext, "Logging out...", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Unregister by getting username to remove from firebase
        val userVal = getIntent()
        username = userVal.getStringExtra("username")!!
        unregister = findViewById(R.id.unregister)

        unregister.setOnClickListener {
            unregisterAccount()
        }

    }

    // Report violation and search if address exists
    fun reportViolation(addr: String, violation: String) {
        val database = Firebase.database.reference
        val usersRef: DatabaseReference = database.child("users")
        val userRef = usersRef.child("residents")

        val sdf = SimpleDateFormat("M/dd/yyy")
        val currDate = sdf.format(Date())

        var notFound = true

        // Checks if address exists
        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children) {
                    if (user.key == addr) {
                        Log.i(TAG, "ADDRESS FOUND")
                        var violations = user.child("violations")
                        val addViolations = usersRef.child("residents/" + addr + "/violations/Notification " + (violations.childrenCount + 10))
                        notFound = false
                        addViolations.setValue(currDate.toString() + ": " + violation)
                        break
                    }
                }

                if (notFound) {
                    Log.i(TAG, "Could not find user with address")
                    Toast.makeText(applicationContext, "User not found with this address", Toast.LENGTH_SHORT).show()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.i(ResidentRegister.TAG, error.message)
            }
        })

        //resetting textView items to be blank
        var add = findViewById(R.id.address) as TextView
        add.text = ""
        var city = findViewById(R.id.city) as TextView
        city.text = ""
        var CS = findViewById(R.id.countryState) as TextView
        CS.text = ""
        var zip = findViewById(R.id.zipCode) as TextView
        zip.text = ""
        var vio = findViewById(R.id.violation) as TextView
        vio.text = ""

        Log.i(TAG, "Completed Report Violation function")

    }

    // Removes account by searching until finding username associated with firebase account
    fun unregisterAccount() {
        val database = Firebase.database.reference
        val usersRef: DatabaseReference = database.child("users")
        val userRef = usersRef.child("collectors")

        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var found = false

                for (user in snapshot.children) {
                    if (user.key.toString() == username) {
                        found = true
                        user.ref.removeValue()
                        break
                    }
                }

                if (found) {
                    Toast.makeText(applicationContext, "Account unregistered", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i(ResidentRegister.TAG, error.message)
            }
        })

    }

    // Gets result from GCMap and converts it to an address to be used and report violation
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            val fullAddress = data?.getStringExtra("address")
            val addrSplit = fullAddress!!.split(",")
            val csz = addrSplit[2].trim().split(" ")
            address.setText(addrSplit[0].trim())
            city.setText(addrSplit[1].trim())
            countryState.setText(csz[0])
            zipCode.setText(csz[1])
        }
    }

    // Disable back button
    override fun onBackPressed() {
        if (false) {
            super.onBackPressed()
        }
    }

    companion object {
        private const val REQUEST_CODE = 1
        const val TAG = "Recycling Reminder"
    }
}