package com.example.recyreminder

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import java.io.*
import android.widget.Button
import android.widget.EditText
import android.util.Log
import com.example.recyreminder.ui.login.LoginActivity

class GarbageCollectorCompany: Activity() {

    protected lateinit var company: EditText
    protected lateinit var cont: Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gc_company)

        Log.i(TAG, "Entered Company Selection")

        company = findViewById(R.id.company)
        cont = findViewById(R.id.cont)
        cont.setOnClickListener {
            val button = Intent(
                this@GarbageCollectorCompany,
                LoginActivity::class.java
            )
            startActivity(button)
        }

    }

    companion object {
        private val TAG = "Garbage Collector Company"
    }

}