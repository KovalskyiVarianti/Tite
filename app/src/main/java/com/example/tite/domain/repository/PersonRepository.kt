package com.example.tite.domain.repository

import com.example.tite.domain.entities.PersonEntity
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    val personList: Flow<List<PersonEntity>>
    val personInfo : Flow<PersonEntity?>
    fun addPersonInfoListener(personUID : String)
}