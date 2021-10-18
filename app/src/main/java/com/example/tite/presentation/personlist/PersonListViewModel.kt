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

    private fun List<PersonEntity>.asPersonItemList() = map { person ->
        PersonListItem.PersonItem(person.uid, person.name, person.email, person.photo)
    }

    fun createChat(person: PersonListItem.PersonItem) {
        viewModelScope.launch {

            chatRepository.createChat(userManager.asPersonEntity(), person.asPersonEntity())
        }
    }

    private fun PersonListItem.PersonItem.asPersonEntity() =
        PersonEntity(uid, personName, personEmail, personImageUrl)

    private fun UserManager.asPersonEntity() =
        PersonEntity(userUID.orEmpty(), name.orEmpty(), userEmail.orEmpty(), photoUrl.toString())
}