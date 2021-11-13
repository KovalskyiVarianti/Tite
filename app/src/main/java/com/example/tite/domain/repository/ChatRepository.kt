package com.example.tite.domain.repository

import com.example.tite.domain.entities.ChatEntity
import com.example.tite.domain.entities.PersonEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val chatEntityList: Flow<List<ChatEntity>>
    fun addChatListener(uid: String)
    fun removeChatListener(uid: String)
    suspend fun addToContactAndCreateChat(
        selfPerson: PersonEntity,
        person: PersonEntity,
        relation: String,
        onChatIdCreated: (chatId: String) -> Unit
    )
}