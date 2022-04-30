package com.example.recyreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.recyreminder.data.PositionLogin
import com.example.recyreminder.ui.login.LoginActivity
import java.io.*

class SelectPage: Activity() {
    protected lateinit var gcButton: Button
    protected lateinit var residentButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_page)

        Log.i(TAG, "Entered Select Page")

        gcButton = findViewById(R.id.gc)
        residentButton = findViewById(R.id.resident)

        gcButton.setOnClickListener {
            selectPosition(it)
        }
        residentButton.setOnClickListener {
            selectPosition(it)
        }

    }

    fun selectPosition(v: View) {
        with (v as Button) {
            val button: Intent
            button = Intent(
                this@SelectPage,
                PositionLogin::class.java
            )
            button.putExtra("position", v.text)
            startActivity(button)
        }
    }

    companion object {
        private val TAG = "Select Page"
    }

}