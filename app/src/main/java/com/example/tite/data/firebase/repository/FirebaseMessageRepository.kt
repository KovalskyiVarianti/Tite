package com.example.tite.data.firebase.repository

import com.example.tite.data.firebase.database.FirebaseMessageDatabase
import com.example.tite.data.firebase.database.MessageDBEntity
import com.example.tite.data.firebase.database.PersonDBEntity
import com.example.tite.domain.entities.MessageEntity
import com.example.tite.domain.repository.MessageRepository
import com.example.tite.domain.entities.PersonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FirebaseMessageRepository(private val messageDatabase: FirebaseMessageDatabase) :
    MessageRepository {

    override val messageEntityList = messageDatabase.messageDBEntityList.map { list ->
        list.map { it.asMessageEntity() }
    }

    override suspend fun sendMessage(chatId: String, message: MessageEntity) {
        withContext(Dispatchers.IO) {
            messageDatabase.sendMessage(chatId, message.asMessageDBEntity())
        }
    }

    override fun addMessageListener(chatId: String) {
        messageDatabase.addMessageListener(chatId)
    }

    override fun removeMessageListener(chatId: String) {
        messageDatabase.removeMessageListener(chatId)
    }

    private fun MessageEntity.asMessageDBEntity() =
        MessageDBEntity(id, sender.asPersonDBEntity(), receiverUID, text)

    private fun MessageDBEntity.asMessageEntity() =
        MessageEntity(
            id,
            sender?.asPersonEntity() ?: PersonEntity("", "", "", ""),
            receiverUID.orEmpty(),
            text.orEmpty()
        )

    private fun PersonEntity.asPersonDBEntity() =
        PersonDBEntity(uid, name, email, photo)

    private fun PersonDBEntity.asPersonEntity() =
        PersonEntity(uid.orEmpty(), name.orEmpty(), email.orEmpty(), photoUrl.orEmpty())
}