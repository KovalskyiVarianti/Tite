package com.example.tite.data.firebase.repository

import com.example.tite.data.firebase.database.FirebasePersonDatabase
import com.example.tite.data.firebase.database.PersonDBEntity
import com.example.tite.domain.UserManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class FirebaseAuthRepository(
    private val firebasePersonDatabase: FirebasePersonDatabase,
    private val firebaseAuth: FirebaseAuth,
    private val userManager: UserManager,
) {

    private val job = Job()
    private val coroutinesScope = CoroutineScope(Dispatchers.IO + job)

    suspend fun logIn(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    suspend fun createUser(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { coroutinesScope.launch { createCurrentUserPerson() } }
    }

    fun signOut() = coroutinesScope.launch {
        firebaseAuth.signOut()
    }

    private suspend fun createCurrentUserPerson() {
        firebasePersonDatabase.createPerson(userManager.getPersonDBEntity())
    }

    private fun UserManager.getPersonDBEntity() = PersonDBEntity(
        userUID.orEmpty(), name.orEmpty(), userEmail.orEmpty(), photoUrl.toString()
    )
}