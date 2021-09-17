package com.example.tite.presentation.extensions

import android.app.Activity
import android.widget.Toast

fun Activity.showToast(text: String, duration: Int) = Toast.makeText(this, text, duration)