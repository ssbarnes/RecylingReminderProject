package com.example.recyreminder.data

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.recyreminder.R
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class ResidentNotifications : Activity(){
    private var database: DatabaseReference = Firebase.database.reference

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resident_notification)

        //Set up RecyclerView
        val mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        // TODO - Another proof of concept. This is how you would iterate through the
        //  resident addresses
        val residentsRef: DatabaseReference = database.child("users/residents")
        residentsRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (address in snapshot.children) {
                    Log.i(PositionLogin.TAG, address.key.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(PositionLogin.TAG, error.message)
            }
        })

        //Set the layout manager
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the adapter
        val names = ArrayList<String>()

        //Here is where a list of notifications from firebase would be shown instead
        names.add("notification 1")
        names.add("notification 2")
        names.add("notification 3")
        names.add("notification 3")
        names.add("notification 3")
        names.add("notification 3")
        names.add("notification 3")
        names.add("notification 3")
        names.add("notification 3")
        names.add("notification 3")
        names.add("notification 3")

        mRecyclerView.adapter = MyRecyclerViewAdapter(names,R.layout.notification)
    }

}