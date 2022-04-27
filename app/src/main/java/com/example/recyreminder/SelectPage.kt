package com.example.recyreminder

import android.app.Activity
import android.os.Bundle
import android.util.Log

class FirstPage: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_page)

//        Log.i(TAG)
    }

    companion object {
        private val TAG = "Select Page"
    }

}