package com.example.myapplication

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

class MainFragment: Fragment(R.layout.main_fragment) {
    private lateinit var progressBar: ProgressBar
    private lateinit var mainImage: ImageView
    private lateinit var mainText: TextView

    private lateinit var blockForwardButtonCallback: BlockForwardButtonDuringLoad

    enum class Categories {
        LATEST,
        TOP,
        HOT  // Not working - empty response from backend
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BlockForwardButtonDuringLoad) {
            blockForwardButtonCallback = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = requireView().findViewById(R.id.loadingProgressBar)
        mainImage = requireView().findViewById(R.id.mainImage)
        mainText = requireView().findViewById(R.id.mainText)

        blockForwardButtonCallback.disableForwardButton()
        main()
    }

    private fun main() {
        showProgressBar()

        val url = getUrl()
        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                try {
                    loadImage(response)
                } catch (e: Exception) {
                    Log.d("Tinkoff-lab", "main -> glide -> error: ${e.message}")
                    Toast.makeText(context, "Error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                    blockForwardButtonCallback.enableForwardButton()
                    onLoadingError()
                }
            },
            {
                blockForwardButtonCallback.enableForwardButton()
                Log.d("Tinkoff-lab", "main -> glide -> error: ${it.message}")
                Toast.makeText(context, "Error occurred: ${it.message}", Toast.LENGTH_LONG).show()
                onLoadingError()
            }
        )

        queue.add(stringRequest)
    }

    private fun getUrl(): String {
        var url = "https://developerslife.ru/"
        url += when (Categories.values()[requireArguments().getInt("category_id")]) {
            Categories.LATEST -> "latest"
            Categories.TOP -> "top"
            Categories.HOT -> "hot"  // Not working
        }
        url += "/"
        url += Random.nextInt(1000).toString()
        url += "?json=true"

        return url
    }

    private fun loadImage(json: String) {
        val jsonArray = JSONObject(json)["result"] as JSONArray

        // Note: items could potentially be the same
        val randomIndex = Random.nextInt(jsonArray.length())
        val randomPost = jsonArray[randomIndex] as JSONObject
        val postText = randomPost.get("description") as String
        val gifUrl = randomPost.get("gifURL") as String

        Glide.with(this)
            .load(gifUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    blockForwardButtonCallback.enableForwardButton()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    hideProgressBar()
                    blockForwardButtonCallback.enableForwardButton()
                    return false
                }
            })
            .into(mainImage)

        mainText.text = postText
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
        mainImage.visibility = View.INVISIBLE
        mainText.visibility = View.GONE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        mainImage.visibility = View.VISIBLE
        mainText.visibility = View.VISIBLE
    }
}