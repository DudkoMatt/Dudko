package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), ReloadCallback, HideButtonsCallback, BlockForwardButtonDuringLoad {
    private lateinit var backButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var tabLayout: TabLayout
    private var currentFragmentIndex: Int = -1
    private var maxFragmentIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize variables
        backButton = findViewById(R.id.backButton)
        forwardButton = findViewById(R.id.forwardButton)
        tabLayout = findViewById(R.id.tabLayout)

        // Setup onClickListeners
        disableBackButton()
        backButton.setOnClickListener {
            handleBack()
        }

        forwardButton.setOnClickListener {
            loadImage()
            enableBackButton()
        }

        // Setup tabs
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) return

                // Option 1: handle stack of tabLayout indexes for correct undo + error fragment
                // Handle error fragment
//                val lastFragment = supportFragmentManager.fragments.lastOrNull()
//                if (lastFragment != null && lastFragment is ErrorFragment) {
//                    supportFragmentManager.popBackStack()
//                }

                // Option 2: Erase all fragments on changing category
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                currentFragmentIndex = -1
                maxFragmentIndex = -1

                disableBackButton()
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
    }

    override fun loadImage() {
        // Setup fragment container
        findViewById<ImageButton>(R.id.forwardButton).visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.backButton).visibility = View.VISIBLE

        when {
            currentFragmentIndex == maxFragmentIndex -> {
                // Create new fragment in that case
                val bundle = bundleOf("category_id" to tabLayout.selectedTabPosition)
                ++maxFragmentIndex

                val fragmentToHide = supportFragmentManager.findFragmentByTag(currentFragmentIndex.toString())
                if (fragmentToHide != null) {
                    supportFragmentManager.commit {
                        hide(fragmentToHide)
                    }
                }

                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<MainFragment>(R.id.fragmentContainer, args = bundle, tag = maxFragmentIndex.toString())
                    addToBackStack(null)
                }
            }
            currentFragmentIndex < maxFragmentIndex -> {
                // Show previous from back stack
                val fragmentToHide = supportFragmentManager.findFragmentByTag(currentFragmentIndex.toString())
                val fragmentToShow = supportFragmentManager.findFragmentByTag((currentFragmentIndex + 1).toString())
                if (fragmentToHide != null && fragmentToShow != null) {
                    supportFragmentManager.commit {
                        hide(fragmentToHide)
                        show(fragmentToShow)
                    }
                }
            }
            else -> {
                throw IllegalArgumentException("currentFragmentIndex > maxFragmentIndex - wrong state")
            }
        }

        ++currentFragmentIndex
    }

    override fun onBackPressed() {
        // Exit if there's only one left
        if (currentFragmentIndex == 0) {
            finish()
        }

        handleBack()
    }

    private fun handleBack() {
        // Disable button if only one will be in stack
        if (currentFragmentIndex == 1) {
            disableBackButton()
        }

        // Hide top fragment and show other
        val fragmentToHide = supportFragmentManager.findFragmentByTag(currentFragmentIndex.toString())
        val fragmentToShow = supportFragmentManager.findFragmentByTag((currentFragmentIndex - 1).toString())
        if (fragmentToHide != null && fragmentToShow != null) {
            supportFragmentManager.commit {
                hide(fragmentToHide)
                show(fragmentToShow)
            }
        }
        --currentFragmentIndex
    }

    fun disableBackButton() {
        backButton.isEnabled = false
        backButton.imageTintList = applicationContext.getColorStateList(R.color.grey)
    }

    fun enableBackButton() {
        backButton.isEnabled = true
        backButton.imageTintList = applicationContext.getColorStateList(R.color.yellow)
    }

    override fun disableForwardButton() {
        forwardButton.isEnabled = false
        forwardButton.imageTintList = applicationContext.getColorStateList(R.color.grey)
    }

    override fun enableForwardButton() {
        forwardButton.isEnabled = true
        forwardButton.imageTintList = applicationContext.getColorStateList(R.color.green)
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