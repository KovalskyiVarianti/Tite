package com.example.tite.data.network.firebase.database

import android.net.Uri
import com.example.tite.domain.UserManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber

class FirebasePersonDatabase(
    firebaseRTDB: FirebaseDatabase,
    firebaseStorage: FirebaseStorage,
    private val userManager: UserManager
) {
    private val personDB = firebaseRTDB.getReference(PERSON_ROOT)
    private val avatarDB = firebaseStorage.getReference(AVATAR_ROOT)

    private val _personDBEntityList =
        MutableSharedFlow<List<PersonDBEntity>>(1, 0, BufferOverflow.DROP_OLDEST)
    val personDBEntityList = _personDBEntityList.asSharedFlow()

    private val _personDBInfo = MutableSharedFlow<PersonDBEntity>(1, 0, BufferOverflow.DROP_OLDEST)
    val personDBInfo = _personDBInfo.asSharedFlow()

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val personListListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            coroutineScope.launch {
                val personList = mutableListOf<PersonDBEntity>()
                for (personsChild in snapshot.children) {
                    if (personsChild.key.equals(userManager.userUID)) {
                        continue
                    }
                    personList.add(personsChild.getValue(PersonDBEntity::class.java) ?: continue)
                }
                _personDBEntityList.emit(personList)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Timber.d("Data loading error: ${error.message}")
        }
    }

    private val personInfoListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            coroutineScope.launch {
                val person = snapshot.getValue(PersonDBEntity::class.java)
                if (person != null) {
                    _personDBInfo.emit(person)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Timber.d("New data ${error.message}")
        }
    }

    private val personPhotoListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            coroutineScope.launch {

            }
        }

        override fun onCancelled(error: DatabaseError) {

        }

    }

    suspend fun createPerson(personDBEntity: PersonDBEntity) = withContext(Dispatchers.IO) {
        personDB.child(personDBEntity.uid.orEmpty()).setValue(personDBEntity)
    }

    fun addPersonListListener() {
        personDB.addValueEventListener(personListListener)
    }

    fun removePersonListListener() {
        personDB.removeEventListener(personListListener)
    }

    fun addPersonInfoListener(personUID: String) {
        personDB.child(personUID).addValueEventListener(personInfoListener)
    }

    fun removePersonInfoListener(personUID: String) {
        personDB.child(personUID).removeEventListener(personInfoListener)
    }

    fun uploadAvatar(uri: Uri, onSuccess: (avatarUri : Uri) -> Unit) {
        avatarDB.child(userManager.userUID.orEmpty())
            .putFile(uri)
            .addOnSuccessListener { snapshot ->
                snapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    personDB.child(userManager.userUID.orEmpty())
                        .child(AVATAR)
                        .setValue(uri.toString())
                        .addOnSuccessListener {
                            userManager.updatePhoto(uri)
                            onSuccess(uri)
                        }
                    Timber.d("Photo uploaded: $uri")
                }
            }
    }


    private companion object {
        const val PERSON_ROOT = "persons"
        const val AVATAR_ROOT = "avatars"
        const val AVATAR = "photoUri"
    }
}