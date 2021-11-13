package com.example.tite.data.network.firebase.repository

import com.example.tite.data.network.firebase.database.ChatDBEntity
import com.example.tite.data.network.firebase.database.FirebaseChatDatabase
import com.example.tite.data.network.firebase.database.PersonDBEntity
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

    override suspend fun addToContactAndCreateChat(
        selfPerson: PersonEntity,
        person: PersonEntity,
        relation: String,
        onChatIdCreated: (chatId: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            chatDatabase.addToContactAndCreateChat(
                selfPerson.asPersonDBEntity(),
                person.asPersonDBEntity(),
                relation,
                onChatIdCreated
            )
        }
    }

    private fun ChatDBEntity.asChatEntity() =
        ChatEntity(id.orEmpty(), members.orEmpty(), message.orEmpty())

    private fun PersonEntity.asPersonDBEntity() =
        PersonDBEntity(uid, name, email, photo)

    private fun PersonDBEntity.asPersonEntity() =
        PersonEntity(uid.orEmpty(), name.orEmpty(), email.orEmpty(), photoUri.orEmpty())
}



