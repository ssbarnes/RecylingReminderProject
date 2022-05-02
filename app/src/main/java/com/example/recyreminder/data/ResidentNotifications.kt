package com.example.recyreminder.data

import android.app.Activity
import android.os.Bundle
import com.example.recyreminder.R
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*


class ResidentNotifications : Activity(){

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resident_notification)

        //Set up RecyclerView
        val mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view)

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