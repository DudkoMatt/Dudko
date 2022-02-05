package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainFragment: Fragment(R.layout.main_fragment) {
    private lateinit var progressBar: ProgressBar
    private lateinit var mainImage: ImageView
    private lateinit var mainText: TextView

    enum class Categories {
        LATEST,
        BEST,
        HOT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = requireView().findViewById(R.id.loadingProgressBar)
        mainImage = requireView().findViewById(R.id.mainImage)
        mainText = requireView().findViewById(R.id.mainText)

        main()
    }

    private fun main() {
        showProgressBar()

        val url = getUrl()
        Toast.makeText(context, url, Toast.LENGTH_SHORT).show()  // ToDO: remove
        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                mainText.text = "Response is OK"
                hideProgressBar()
            },
            {
                onLoadingError()
            }
        )

        queue.add(stringRequest)

        // got url
        // get json
        // parse json
        // load image
        // handle error on each step
    }

    private fun getUrl(): String {
        var url = "https://developerslife.ru/"
        url += when (Categories.values()[requireArguments().getInt("category_id")]) {
            Categories.LATEST -> "latest"
            Categories.BEST -> "daily"
            Categories.HOT -> "hot"
        }
        url += "/"
        url += Random.nextInt(1000).toString()
        url += "?json=true"

        return url
    }

    private fun loadImage(url: String) {

    }

    private fun onLoadingError() {
        parentFragmentManager.popBackStack()
        parentFragmentManager.commit {
            add<ErrorFragment>(R.id.fragmentContainer)
            addToBackStack(null)
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        mainImage.visibility = View.GONE
        mainText.visibility = View.GONE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        mainImage.visibility = View.VISIBLE
        mainText.visibility = View.VISIBLE
    }
}