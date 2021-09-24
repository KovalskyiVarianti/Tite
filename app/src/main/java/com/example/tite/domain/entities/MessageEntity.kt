package com.example.tite.domain.entities

data class MessageEntity(
    val id: String? = null,
    val sender: PersonEntity,
    val receiverUID : String,
    val text: String
)