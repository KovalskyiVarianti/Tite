package com.example.tite.presentation.personlist


import androidx.lifecycle.ViewModel
import com.example.tite.data.firebase.repository.FirebasePersonRepository
import com.example.tite.domain.PersonEntity
import com.example.tite.domain.UserManager
import kotlinx.coroutines.flow.map

class PersonListViewModel(
    private val personRepository: FirebasePersonRepository,
    private val userManager: UserManager
) : ViewModel() {
    val personList = personRepository.personList.map {it.asPersonItemList()}

    private fun List<PersonEntity>.asPersonItemList() = map { person ->
        if (userManager.userEmail == person.email) {
            PersonListItem.PersonItem("SELF", person.photo, person.email)
        } else {
            PersonListItem.PersonItem(person.name, person.photo, person.email)
        }
    }
}