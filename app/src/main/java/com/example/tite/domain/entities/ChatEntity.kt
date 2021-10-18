package com.example.tite.domain.entities

data class ChatEntity(
    val id: String,
    val members: List<String>,
    val topMessage: String,
)