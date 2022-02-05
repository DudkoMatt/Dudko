package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class ErrorFragment: Fragment(R.layout.load_error_fragment) {
    private var callback: ReloadCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ReloadCallback) {
            callback = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Toast.makeText(context, "Reload - It works!", Toast.LENGTH_SHORT).show()  // ToDO: remove
        requireView().findViewById<Button>(R.id.retryButton).setOnClickListener {
            parentFragmentManager.popBackStack()
            callback?.loadImage()
        }
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }
}