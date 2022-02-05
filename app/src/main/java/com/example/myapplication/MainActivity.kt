package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup onClickListeners
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            Toast.makeText(applicationContext, "Back - It works!", Toast.LENGTH_LONG).show()
        }

        findViewById<ImageButton>(R.id.forwardButton).setOnClickListener {
            Toast.makeText(applicationContext, "Forward - It works!", Toast.LENGTH_LONG).show()
        }
    }
}