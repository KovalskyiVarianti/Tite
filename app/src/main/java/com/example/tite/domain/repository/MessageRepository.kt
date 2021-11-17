package com.example.tite.domain.repository

import com.example.tite.data.network.NotificationData
import com.example.tite.domain.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    val messageEntityList: Flow<List<MessageEntity>>
    suspend fun sendMessage(
        message: MessageEntity,
        notification: NotificationData
    )
    fun subscribeOnNotifications(userUID: String)
    fun addMessageListener(chatId: String)
    fun removeMessageListener(chatId: String)
}