package com.sherlock.androidprogramming4e.geoquiz

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AboutActivity : AppCompatActivity() {
    private lateinit var apiLevelTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        apiLevelTextView = findViewById(R.id.api_level)
        apiLevelTextView.setText(" " + Build.VERSION.SDK_INT.toString())
    }
}