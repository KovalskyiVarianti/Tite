package com.example.tite.data.network.firebase.database

import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class PersonDBEntity(
    val uid: String? = "",
    val name: String? = "",
    val email: String? = "",
    val photoUri: String? = null,
)

@Parcelize
@IgnoreExtraProperties
data class ChatDBEntity(
    val id: String? = "",
    val members: List<String>? = listOf(),
    val message: String? = null
)

@Parcelize
@IgnoreExtraProperties
data class MessageDBEntity(
    val id: String? = "",
    val senderUID: String? = "",
    val receiverUID: String? = "",
    val text: String? = "",
)