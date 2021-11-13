package com.example.tite.presentation.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.showToast(text: String, duration: Int) = Toast.makeText(this, text, duration)

fun Activity.hideKeyboard() {
    ContextCompat.getSystemService(this, InputMethodManager::class.java)?.
    hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}