package com.example.tite.data.firebase.database

import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class PersonDBEntity(
    val uid: String? = "",
    val name: String? = "",
    val email: String? = "",
    val photoUrl: String? = "",
)

@Parcelize
@IgnoreExtraProperties
data class ChatDBEntity(
    val id: String? = "",
    val members: List<PersonDBEntity>? = listOf(),
    val message: String? = null
)

@Parcelize
@IgnoreExtraProperties
data class MessageDBEntity(
    val id: String? = "",
    val sender: PersonDBEntity? = null,
    val receiverUID: String? = "",
    val text: String? = "",
)