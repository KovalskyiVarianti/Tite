package com.example.tite.data.network.firebase.repository

import com.example.tite.data.network.firebase.database.FirebasePersonDatabase
import com.example.tite.data.network.firebase.database.PersonDBEntity
import com.example.tite.domain.repository.AuthRepository
import com.example.tite.domain.AuthState
import com.example.tite.domain.UserManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

class FirebaseAuthRepository(
    private val firebasePersonDatabase: FirebasePersonDatabase,
    private val firebaseAuth: FirebaseAuth,
    private val userManager: UserManager,
) : AuthRepository {

    private val job = Job()
    private val coroutinesScope = CoroutineScope(Dispatchers.IO + job)

    override val authState: MutableStateFlow<AuthState> = if (userManager.isLoggedIn) {
        MutableStateFlow(AuthState.LoggedIn)
    } else {
        MutableStateFlow(AuthState.LoggedOut)
    }

    override suspend fun logIn(email: String, password: String) {
        withContext(Dispatchers.IO) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authState.value = AuthState.LoggedIn
                } else {
                    authState.value = AuthState.Failure(task.exception?.message.orEmpty())
                }
            }
        }
    }

    override suspend fun createUser(nickname: String, email: String, password: String) {
        withContext(Dispatchers.IO) {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        coroutinesScope.launch {
                            createCurrentUserPerson(nickname)
                            authState.value = AuthState.LoggedIn
                        }
                    } else {
                        authState.value = AuthState.Failure(task.exception?.message.orEmpty())
                    }
                }
        }
    }

    private suspend fun createCurrentUserPerson(nickname: String) {
        userManager.updateName(nickname)
        firebasePersonDatabase.createPerson(userManager.getPersonDBEntity(nickname))
    }

    private fun UserManager.getPersonDBEntity(nickname: String) = PersonDBEntity(
        userUID.orEmpty(), nickname, userEmail.orEmpty(), photoUri.toString()
    )
}
