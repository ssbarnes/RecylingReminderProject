package com.example.recyreminder.data

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
    private var id = "id"

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

        val notifIntent = Intent(this, ResidentNotifications::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_IMMUTABLE)


        var builder = NotificationCompat.Builder(this, "id")
            .setSmallIcon(R.drawable.rectangle)
            .setContentTitle("Trash Violation!")
            .setContentText("You have received a trash violation!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        createNotificationChannel()


        // Display all user notifications
        violationsRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (violation in snapshot.children) {
                    names.add(0, violation.value as String)
                    adapter.notifyItemChanged(0)
                }
                //send the notification
                with(NotificationManagerCompat.from(applicationContext)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(1, builder.build())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(PositionLogin.TAG, error.message)
            }
        })
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notifications"
            val descriptionText = "For sending trash violation notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("id", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}