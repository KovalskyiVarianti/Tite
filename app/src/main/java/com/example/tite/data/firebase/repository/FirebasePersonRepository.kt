package com.example.tite.data.firebase.repository

import com.example.tite.data.firebase.database.FirebasePersonDatabase
import com.example.tite.data.firebase.database.PersonDBEntity
import com.example.tite.domain.PersonEntity
import kotlinx.coroutines.flow.map

class FirebasePersonRepository(private val personDatabase: FirebasePersonDatabase) {
    val personList = personDatabase.personDBEntityList.map { it.asPersonEntityList() }

    private fun List<PersonDBEntity>.asPersonEntityList() =
        map { person ->
            PersonEntity(
                person.name.orEmpty(),
                person.email.orEmpty(),
                person.photoUrl.orEmpty()
            )
        }

}