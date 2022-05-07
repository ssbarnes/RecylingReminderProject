package com.example.recyreminder.data

import android.util.Log
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.recyreminder.R
import android.widget.EditText
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

    private lateinit var address: EditText
    private lateinit var city: EditText
    private lateinit var countryState: EditText
    private lateinit var zipCode: EditText
    private lateinit var violation: EditText
    private lateinit var report : Button
    private lateinit var viewMap : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gc_menu)

        Log.i(TAG, "Entered GC Menu/Reporting Violation")

        address = findViewById(R.id.address)
        city = findViewById(R.id.city)
        countryState = findViewById(R.id.countryState)
        zipCode = findViewById(R.id.zipCode)

        report = findViewById(R.id.report)
        report.setOnClickListener {
            violation = findViewById(R.id.violation)

            val addr = address.text.toString() + ", " + city.text.toString() + ", " +
                    countryState.text.toString() + " " + zipCode.text.toString()

            reportViolation(addr, violation.text.toString())

        }

        viewMap = findViewById(R.id.map)
        viewMap.setOnClickListener {
            val mapIntent = Intent(
                this@GCReportViolation,
                GCMap::class.java
            )

            startActivityForResult(mapIntent, 1)
        }
    }

    fun reportViolation(addr: String, violation: String) {
        val database = Firebase.database.reference
        val usersRef: DatabaseReference = database.child("users")
        val userRef = usersRef.child("residents")

        val sdf = SimpleDateFormat("M/dd/yyy")
        val currDate = sdf.format(Date())

        var notFound = true

        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children) {
                    if (user.key == addr) {
                        Log.i(TAG, "ADDRESS FOUND")
                        var violations = user.child("violations")
                        val addViolations = usersRef.child("residents/" + addr + "/violations/Notification " + (violations.childrenCount + 1))
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

        Log.i(TAG, "Completed Report Violation function")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            val fullAddress = data.getStringExtra("address")
            val addrSplit = fullAddress!!.split(",")
            val csz = addrSplit[2].trim().split(" ")
            Log.i(TAG, "FULL: " + fullAddress + " SPLIT: " + addrSplit + " SPLITAGAIN: " + csz)
            address.setText(addrSplit[0].trim())
            city.setText(addrSplit[1].trim())
            countryState.setText(csz[0])
            zipCode.setText(csz[1])
        }
    }

    companion object {
        private const val REQUEST_CODE = 1
        const val TAG = "Recycling Reminder"
    }
}