package com.example.recyreminder.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyreminder.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ResidentNotifications : Activity(){
    private var database: DatabaseReference = Firebase.database.reference
    private lateinit var mPrefs: SharedPreferences

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPrefs = getSharedPreferences("appPrefs", Context.MODE_PRIVATE)

        setContentView(R.layout.resident_notification)

        //Set up RecyclerView
        val mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        //Set the layout manager
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the adapter
        val names = ArrayList<String>()
        var adapter = MyRecyclerViewAdapter(names,R.layout.notification)
        mRecyclerView.adapter = adapter


        val savedAddress = mPrefs.getString("address", "").toString()
        Log.i(PositionLogin.TAG, savedAddress)

        // Get violations from particular user
        val violationsRef: DatabaseReference =
            database.child("users/residents/$savedAddress/violations")

        // Display all user notifications
        violationsRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (violation in snapshot.children) {
                    names.add(0, violation.value as String)
                    adapter.notifyItemChanged(0)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(PositionLogin.TAG, error.message)
            }
        })
    }

}