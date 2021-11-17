package com.example.tite.data.network.firebase.repository

import com.example.tite.data.network.Notification
import com.example.tite.data.network.NotificationData
import com.example.tite.data.network.RetrofitNotificationApi
import com.example.tite.data.network.RetrofitNotificationApi.Companion.TOPICS
import com.example.tite.data.network.firebase.database.FirebaseMessageDatabase
import com.example.tite.data.network.firebase.database.MessageDBEntity
import com.example.tite.data.network.firebase.database.PersonDBEntity
import com.example.tite.domain.entities.MessageEntity
import com.example.tite.domain.entities.PersonEntity
import com.example.tite.domain.repository.MessageRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FirebaseMessageRepository(
    private val messageDatabase: FirebaseMessageDatabase,
    private val firebaseMessaging: FirebaseMessaging,
    private val retrofitNotificationApi: RetrofitNotificationApi,
) :
    MessageRepository {

    override val messageEntityList = messageDatabase.messageDBEntityList.map { list ->
        list.map { it.asMessageEntity() }
    }

    override suspend fun sendMessage(
        message: MessageEntity,
        notification: NotificationData,
    ) {
        withContext(Dispatchers.IO) {
            messageDatabase.sendMessage(notification.chat, message.asMessageDBEntity())
            retrofitNotificationApi.sendNotification(
                Notification(notification,
                    TOPICS + message.receiverUID
                )
            )
        }
    }

    override fun subscribeOnNotifications(userUID: String) {
        firebaseMessaging.subscribeToTopic(TOPICS + userUID)
    }

    override fun addMessageListener(chatId: String) {
        messageDatabase.addMessageListener(chatId)
    }

    override fun removeMessageListener(chatId: String) {
        messageDatabase.removeMessageListener(chatId)
    }

    private fun MessageEntity.asMessageDBEntity() =
        MessageDBEntity(id, senderUID, receiverUID, text)

    private fun MessageDBEntity.asMessageEntity() =
        MessageEntity(
            id,
            senderUID.orEmpty(),
            receiverUID.orEmpty(),
            text.orEmpty()
        )
}