package com.example.tite.data.network.firebase.repository

import android.net.Uri
import com.example.tite.data.network.firebase.database.ContactDBEntity
import com.example.tite.data.network.firebase.database.FirebasePersonDatabase
import com.example.tite.data.network.firebase.database.PersonDBEntity
import com.example.tite.domain.entities.ContactEntity
import com.example.tite.domain.entities.PersonEntity
import com.example.tite.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebasePersonRepository(private val personDatabase: FirebasePersonDatabase) :
    PersonRepository {
    override val personList =
        personDatabase.personDBEntityList.map { list -> list.map { it.asPersonEntity() } }

    override val personInfo = personDatabase.personDBInfo.map { it.asPersonEntity() }

    override val contactList =
        personDatabase.contactDBEntityList.map { list -> list.map { it.asContactEntity() } }

    override fun uploadAvatar(uri: Uri, onSuccess: (avatarUri: Uri) -> Unit) {
        personDatabase.uploadAvatar(uri, onSuccess)
    }

    override fun addPersonInfoListener(personUID: String) {
        personDatabase.addPersonInfoListener(personUID)
    }

    override fun removePersonInfoListener(personUID: String) {
        personDatabase.removePersonInfoListener(personUID)
    }

    override fun addPersonListListener() {
        personDatabase.addPersonListListener()
    }

    override fun removePersonListListener() {
        personDatabase.removePersonListListener()
    }

    override fun addContactListListener() {
        personDatabase.addContactListListener()
    }

    override fun removeContactListListener() {
        personDatabase.removeContactListListener()
    }

    private fun PersonDBEntity.asPersonEntity() = PersonEntity(
        uid.orEmpty(),
        name.orEmpty(),
        email.orEmpty(),
        photoUri.orEmpty()
    )

    private fun ContactDBEntity.asContactEntity() = ContactEntity(
        id.orEmpty(),
        relation.orEmpty(),
        chatId.orEmpty(),
    )
}