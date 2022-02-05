package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), ReloadCallback {
    private lateinit var backButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize variables
        backButton = findViewById(R.id.backButton)
        forwardButton = findViewById(R.id.forwardButton)
        tabLayout = findViewById(R.id.tabLayout)

        // Setup onClickListeners
        backButton.setOnClickListener {
            // ToDO
            Toast.makeText(applicationContext, "Back - It works!", Toast.LENGTH_SHORT).show()
        }

        forwardButton.setOnClickListener {
            // ToDO
            Toast.makeText(applicationContext, "Forward - It works!", Toast.LENGTH_SHORT).show()
        }

        // Setup tabs
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) return
                Toast.makeText(applicationContext, "Tab ${tab.position} selected - It works!", Toast.LENGTH_SHORT).show()
                loadImage()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Nothing to do
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }
        })

        // Load image at start
        loadImage()

        // ToDO:
        //  - Button support
        //  - Button disabling
        //  - API support and drawing
        //  - Error handling
    }

    override fun loadImage() {
        // Setup fragment container
        val bundle = bundleOf("category_id" to tabLayout.selectedTabPosition)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainFragment>(R.id.fragmentContainer, args = bundle)
            addToBackStack(null)
        }
    }
}