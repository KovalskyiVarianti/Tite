package com.example.tite.data.firebase.database

import com.example.tite.domain.entities.PersonEntity
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import timber.log.Timber

class FirebasePersonDatabase(
    firebaseRTDB: FirebaseDatabase
) {
    private val personDB = firebaseRTDB.getReference(PERSON_ROOT)

    private val _personDBEntityList =
        MutableSharedFlow<List<PersonDBEntity>>(1, 0, BufferOverflow.DROP_OLDEST)
    val personDBEntityList = _personDBEntityList.asSharedFlow()

    private val _personDBInfo = MutableSharedFlow<PersonDBEntity?>(1, 0, BufferOverflow.DROP_OLDEST)
    val personDBInfo = _personDBInfo.asSharedFlow()

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val personListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            coroutineScope.launch {
                val personList = mutableListOf<PersonDBEntity>()
                for (personsChild in snapshot.children) {
                    personList.add(personsChild.getValue(PersonDBEntity::class.java) ?: continue)
                }
                _personDBEntityList.emit(personList)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Timber.d("New data ${error.message}")
        }
    }

    private val personInfoListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            coroutineScope.launch {
                val person = snapshot.getValue(PersonDBEntity::class.java)
                _personDBInfo.emit(person)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Timber.d("New data ${error.message}")
        }
    }

    init {
        personDB.addValueEventListener(personListener)
    }

    suspend fun createPerson(personDBEntity: PersonDBEntity) = withContext(Dispatchers.IO) {
        personDB.child(personDBEntity.uid.orEmpty()).setValue(personDBEntity)
    }

    fun addPersonInfoListener(personUID: String) {
        personDB.child(personUID).addValueEventListener(personInfoListener)
    }


    private companion object {
        const val PERSON_ROOT = "persons"
    }
}