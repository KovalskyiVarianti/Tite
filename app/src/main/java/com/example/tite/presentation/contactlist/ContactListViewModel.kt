package com.example.tite.presentation.contactlist

import androidx.lifecycle.ViewModel
import com.example.tite.domain.repository.PersonRepository
import kotlinx.coroutines.flow.combine

class ContactListViewModel(private val personRepository: PersonRepository) : ViewModel() {

    val contactList =
        personRepository.contactList.combine(personRepository.personList) { contacts, persons ->
            contacts.map { contactEntity ->
                val person = persons.find { it.uid == contactEntity.contactUID }
                ContactListItem.ContactItem(
                    contactEntity.contactUID,
                    person?.name.orEmpty(),
                    person?.photo.orEmpty(),
                    person?.email.orEmpty(),
                    contactEntity.relation,
                    contactEntity.chatId
                )
            }
        }

    init {
        personRepository.addPersonListListener()
        personRepository.addContactListListener()
    }

    override fun onCleared() {
        super.onCleared()
        personRepository.removePersonListListener()
        personRepository.removeContactListListener()
    }
}