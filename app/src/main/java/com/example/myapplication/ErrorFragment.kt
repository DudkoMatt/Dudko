package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class ErrorFragment: Fragment(R.layout.load_error_fragment) {
    private var callbackReload: ReloadCallback? = null
    private var callbackHideButtons: HideButtonsCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ReloadCallback) {
            callbackReload = context
        }
        if (context is HideButtonsCallback) {
            callbackHideButtons = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Toast.makeText(context, "Reload - It works!", Toast.LENGTH_SHORT).show()  // ToDO: remove
        callbackHideButtons?.hideButtons()
        requireView().findViewById<Button>(R.id.retryButton).setOnClickListener {
            parentFragmentManager.popBackStack()
            callbackReload?.loadImage()
        }
    }

    override fun onDetach() {
        callbackReload = null
        super.onDetach()
    }
}