package com.example.tite.presentation.chatlist

import android.app.Person
import androidx.lifecycle.ViewModel
import com.example.tite.domain.entities.ChatEntity
import com.example.tite.domain.repository.ChatRepository
import com.example.tite.domain.UserManager
import com.example.tite.domain.entities.PersonEntity
import com.example.tite.domain.repository.PersonRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class ChatListViewModel(
    private val chatRepository: ChatRepository,
    private val personRepository: PersonRepository,
    private val userManager: UserManager
) : ViewModel() {

    init {
        chatRepository.addChatListener(userManager.userUID.orEmpty())
    }

    val chatItemList =
        chatRepository.chatEntityList.combine(personRepository.personList) { chats, persons ->
            chats.map { chat ->
                val person = persons.find { person ->
                    person.uid == chat.members.find { it != userManager.userUID }
                } ?: PersonEntity("", "", "", "")
                ChatListItem.ChatItem(chat.id, person.uid, person.name, person.photo, chat.topMessage)
            }
        }

    override fun onCleared() {
        super.onCleared()
        chatRepository.removeChatListener(userManager.userUID.orEmpty())
    }
}