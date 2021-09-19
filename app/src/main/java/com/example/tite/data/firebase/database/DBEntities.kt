package com.example.tite.data.firebase.database

import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class PersonDBEntity(
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null
)