package com.example.tite.presentation.messagelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tite.domain.entities.MessageEntity
import com.example.tite.domain.repository.MessageRepository
import com.example.tite.domain.entities.PersonEntity
import com.example.tite.domain.UserManager
import com.example.tite.domain.repository.ChatRepository
import com.example.tite.domain.repository.PersonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MessageListViewModel(
    private val userManager: UserManager,
    private val messageRepository: MessageRepository,
    private val personRepository: PersonRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {

    val messageItemList = messageRepository.messageEntityList.map { list ->
        list.map { it.asMessageItem() }
    }

    val chatItemMembers = chatRepository.chatEntityList.map { list ->
        list.map { it.id to it.members }
    }

    val personInfo = personRepository.personInfo

    fun addMessageListener(chatId: String) {
        messageRepository.addMessageListener(chatId)
    }

    fun removeMessageListener(chatId: String) {
        messageRepository.removeMessageListener(chatId)
    }

    fun addPersonInfoListener(personUID: String) {
        personRepository.addPersonInfoListener(personUID)
    }

    fun sendMessage(chatId: String, personUID: String, text: String) {
        viewModelScope.launch {
            messageRepository.sendMessage(
                chatId,
                MessageEntity(
                    null,
                    userManager.userUID.orEmpty(),
                    personUID,
                    text
                )
            )
        }
    }

    private fun MessageEntity.asMessageItem() = MessageListItem.MessageItem(
        id.orEmpty(), text, senderUID == userManager.userUID
    )
}