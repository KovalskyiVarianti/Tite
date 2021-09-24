package com.example.tite.data.firebase.repository

import com.example.tite.data.firebase.database.FirebasePersonDatabase
import com.example.tite.data.firebase.database.PersonDBEntity
import com.example.tite.domain.entities.PersonEntity
import com.example.tite.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebasePersonRepository(private val personDatabase: FirebasePersonDatabase) :
    PersonRepository {
    override val personList =
        personDatabase.personDBEntityList.map { list -> list.map { it.asPersonEntity() } }

    override val personInfo = personDatabase.personDBInfo.map { it?.asPersonEntity() }

    override fun addPersonInfoListener(personUID: String) {
        personDatabase.addPersonInfoListener(personUID)
    }

    private fun PersonDBEntity.asPersonEntity() = PersonEntity(
        uid.orEmpty(),
        name.orEmpty(),
        email.orEmpty(),
        photoUrl.orEmpty()
    )
}