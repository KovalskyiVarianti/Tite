package com.example.tite.presentation.persontocontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tite.domain.UserManager
import com.example.tite.domain.entities.PersonEntity
import com.example.tite.domain.repository.ChatRepository
import com.example.tite.domain.repository.PersonRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PersonToContactViewModel(
    private val personRepository: PersonRepository,
    private val chatRepository: ChatRepository,
    private val userManager: UserManager,
) : ViewModel() {

    init {
        personRepository.addContactListListener()
    }

    val personInfo = personRepository.personInfo

    fun addPersonInfoListener(personUID: String) {
        personRepository.addPersonInfoListener(personUID)
    }

    fun removePersonInfoListener(personUID: String) {
        personRepository.removePersonInfoListener(personUID)
    }

    fun addToContactAndCreateChat(
        person: PersonEntity,
        relation: String,
        onChatIdCreated: (chatId: String) -> Unit
    ) {
        viewModelScope.launch {
            personRepository.contactList.collect { contacts ->
                if (contacts.none { it.contactUID == person.uid }){
                    chatRepository.addToContactAndCreateChat(
                        userManager.asPersonEntity(),
                        person,
                        relation,
                        onChatIdCreated
                    )
                }
            }
        }
    }

    private fun UserManager.asPersonEntity() =
        PersonEntity(userUID.orEmpty(), name.orEmpty(), userEmail.orEmpty(), photoUri.orEmpty())

    override fun onCleared() {
        super.onCleared()
        personRepository.removeContactListListener()
    }
}