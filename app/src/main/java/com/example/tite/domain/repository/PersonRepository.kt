package com.example.tite.domain.repository

import android.net.Uri
import com.example.tite.domain.entities.PersonEntity
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    val personList: Flow<List<PersonEntity>>
    val personInfo : Flow<PersonEntity>
    fun uploadAvatar(uri: Uri, onSuccess: (avatarUri : Uri) -> Unit)
    fun addPersonInfoListener(personUID : String)
    fun removePersonInfoListener(personUID: String)
    fun addPersonListListener()
    fun removePersonListListener()
}