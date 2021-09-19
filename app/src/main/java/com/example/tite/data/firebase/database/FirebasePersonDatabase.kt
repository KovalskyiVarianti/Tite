package com.example.tite.data.firebase.database

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

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val valueListener = object : ValueEventListener {
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

    init {
        personDB.addValueEventListener(valueListener)
    }

    suspend fun createPerson(personDBEntity: PersonDBEntity) {
        withContext(Dispatchers.IO) {
            personDB.child(personDBEntity.uid.orEmpty()).setValue(personDBEntity)
        }
    }

    private companion object {
        const val PERSON_ROOT = "persons"
    }
}