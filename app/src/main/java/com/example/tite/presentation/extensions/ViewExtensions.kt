package com.example.tite.presentation.extensions

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.tite.R
import com.example.tite.presentation.contactlist.ContactListItem
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(text: String, duration: Int) = Snackbar.make(this, text, duration).show()

fun ImageView.loadAvatar(uri: String) =
    Glide.with(context)
        .load(uri)
        .transform(CenterCrop(), RoundedCorners(16))
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(R.drawable.loading_image_placeholder)
        .error(R.mipmap.ic_launcher)
        .into(this)

fun ChipGroup.addChip(text: String) {
    Chip(context).apply {
        this.text = text
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setChipBackgroundColorResource(R.color.main_light_color)
        isCheckable = true
    }.also { addView(it) }
}