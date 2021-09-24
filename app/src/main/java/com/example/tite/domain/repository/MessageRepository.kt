package com.example.tite.domain.repository

import com.example.tite.domain.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    val messageEntityList: Flow<List<MessageEntity>>
    suspend fun sendMessage(chatId: String, message: MessageEntity)
    fun addMessageListener(chatId: String)
    fun removeMessageListener(chatId: String)
}