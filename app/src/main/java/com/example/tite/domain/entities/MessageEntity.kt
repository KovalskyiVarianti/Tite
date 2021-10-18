package com.example.tite.domain.entities

data class MessageEntity(
    val id: String? = null,
    val senderUID: String,
    val receiverUID : String,
    val text: String
)