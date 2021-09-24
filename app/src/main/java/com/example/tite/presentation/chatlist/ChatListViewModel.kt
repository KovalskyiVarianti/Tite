package com.example.tite.presentation.chatlist

import androidx.lifecycle.ViewModel
import com.example.tite.domain.entities.ChatEntity
import com.example.tite.domain.repository.ChatRepository
import com.example.tite.domain.UserManager
import kotlinx.coroutines.flow.map

class ChatListViewModel(
    private val chatRepository: ChatRepository,
    private val userManager: UserManager
) : ViewModel() {

    init {
        chatRepository.addChatListener(userManager.userUID.orEmpty())
    }

    val chatItemList = chatRepository.chatEntityList.map { list ->
        list.map { it.asChatItem() }
    }

    private fun ChatEntity.asChatItem(): ChatListItem.ChatItem {
        return if (members.isEmpty()) {
            ChatListItem.ChatItem(
                id, "", "", "", topMessage
            )
        } else {
            val member = members.find { it.uid != userManager.userUID }
            ChatListItem.ChatItem(
                id,
                member?.uid.orEmpty(),
                member?.name.orEmpty(),
                member?.photo.orEmpty(),
                topMessage
            )
        }

    }

    override fun onCleared() {
        super.onCleared()
        chatRepository.removeChatListener(userManager.userUID.orEmpty())
    }
}