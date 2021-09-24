package com.example.tite.data.firebase.repository

import com.example.tite.data.firebase.database.ChatDBEntity
import com.example.tite.data.firebase.database.FirebaseChatDatabase
import com.example.tite.data.firebase.database.PersonDBEntity
import com.example.tite.domain.entities.ChatEntity
import com.example.tite.domain.repository.ChatRepository
import com.example.tite.domain.entities.PersonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FirebaseChatRepository(private val chatDatabase: FirebaseChatDatabase) : ChatRepository {

    override val chatEntityList = chatDatabase.chatDBEntityList.map { list ->
        list.map { it.asChatEntity() }
    }

    override fun addChatListener(uid: String) {
        chatDatabase.addChatListener(uid)
    }

    override fun removeChatListener(uid: String) {
        chatDatabase.removeChatListener(uid)
    }

    override suspend fun createChat(selfPerson: PersonEntity, person: PersonEntity) {
        withContext(Dispatchers.IO) {
            chatDatabase.createChat(selfPerson.asPersonDBEntity(), person.asPersonDBEntity())
        }
    }

    private fun ChatDBEntity.asChatEntity() =
        ChatEntity(id.orEmpty(), members?.map { it.asPersonEntity() }.orEmpty(), message.orEmpty())

    private fun PersonEntity.asPersonDBEntity() =
        PersonDBEntity(uid, name, email, photo)

    private fun PersonDBEntity.asPersonEntity() =
        PersonEntity(uid.orEmpty(), name.orEmpty(), email.orEmpty(), photoUrl.orEmpty())
}


