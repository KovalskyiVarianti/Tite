package com.example.tite.presentation.personlist


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tite.domain.repository.ChatRepository
import com.example.tite.domain.entities.PersonEntity
import com.example.tite.domain.repository.PersonRepository
import com.example.tite.domain.UserManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PersonListViewModel(
    private val personRepository: PersonRepository,
    private val chatRepository: ChatRepository,
    private val userManager: UserManager
) : ViewModel() {
    val personList = personRepository.personList.map { it.asPersonItemList() }

    init {
        chatRepository.addChatListener(userManager.userUID.orEmpty())
        personRepository.addPersonListListener()
    }

    private fun List<PersonEntity>.asPersonItemList() = map { person ->
        PersonListItem.PersonItem(person.uid, person.name, person.email, person.photo)
    }

    fun createChat(person: PersonListItem.PersonItem) {
        viewModelScope.launch {
            chatRepository.chatEntityList.collect { list ->
                if (list.none { it.members.contains(person.uid) }) {
                    chatRepository.createChat(
                        userManager.asPersonEntity(),
                        person.asPersonEntity()
                    ) {
                        
                    }
                }
            }
        }
    }

    private fun PersonListItem.PersonItem.asPersonEntity() =
        PersonEntity(uid, personName, personEmail, personImageUri)

    private fun UserManager.asPersonEntity() =
        PersonEntity(userUID.orEmpty(), name.orEmpty(), userEmail.orEmpty(), photoUri.orEmpty())

    override fun onCleared() {
        super.onCleared()
        chatRepository.removeChatListener(userManager.userUID.orEmpty())
        personRepository.removePersonListListener()
    }
}