package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), ReloadCallback, HideButtonsCallback {
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
            loadImage()
        }

        // Setup tabs
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) return
                Toast.makeText(applicationContext, "Tab ${tab.position} selected - It works!", Toast.LENGTH_SHORT).show()

                // Option 1: handle stack of tabLayout indexes for correct undo + error fragment
                // Handle error fragment
//                val lastFragment = supportFragmentManager.fragments.lastOrNull()
//                if (lastFragment != null && lastFragment is ErrorFragment) {
//                    supportFragmentManager.popBackStack()
//                }

                // Option 2: Erase all fragments on changing category
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                loadImage()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Nothing to do
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Nothing to do
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
        findViewById<ImageButton>(R.id.forwardButton).visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.backButton).visibility = View.VISIBLE

        val bundle = bundleOf("category_id" to tabLayout.selectedTabPosition)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainFragment>(R.id.fragmentContainer, args = bundle)
            addToBackStack(null)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.count() == 1) {
            supportFragmentManager.popBackStack()
        }

        super.onBackPressed()
    }

    override fun hideButtons() {
        backButton.visibility = View.INVISIBLE
        forwardButton.visibility = View.INVISIBLE
    }

    override fun showButtons() {
        backButton.visibility = View.VISIBLE
        forwardButton.visibility = View.VISIBLE
    }


}