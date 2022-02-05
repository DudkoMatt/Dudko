package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.add
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var forwardButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize variables
        backButton = findViewById(R.id.backButton)
        forwardButton = findViewById(R.id.forwardButton)

        // Setup onClickListeners
        backButton.setOnClickListener {
            Toast.makeText(applicationContext, "Back - It works!", Toast.LENGTH_LONG).show()
        }

        forwardButton.setOnClickListener {
            Toast.makeText(applicationContext, "Forward - It works!", Toast.LENGTH_LONG).show()
        }

        // Setup fragment container
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainFragment>(R.id.fragmentContainer)
        }

        // ToDO:
        //  - Button support
        //  - Button disabling
        //  - API support and drawing
        //  - Error handling
    }
}