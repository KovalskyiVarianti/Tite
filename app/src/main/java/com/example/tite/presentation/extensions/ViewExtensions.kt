package com.example.tite.presentation.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(text: String, duration: Int) = Snackbar.make(this, text, duration).show()