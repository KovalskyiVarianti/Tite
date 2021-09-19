package com.example.tite.data.firebase

import android.net.Uri
import com.example.tite.domain.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.*

class FirebaseUserManager(private val firebaseAuth: FirebaseAuth) : UserManager {

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override val userUID
        get() = firebaseAuth.uid

    override val userEmail
        get() = firebaseAuth.currentUser?.email

    override val name
        get() = firebaseAuth.currentUser?.displayName

    override val photoUrl
        get() = firebaseAuth.currentUser?.photoUrl

    override val isLoggedIn
        get() = firebaseAuth.currentUser != null

    override fun updatePhoto(uri: Uri?) {
        coroutineScope.launch {
            firebaseAuth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build()
            )
        }
    }

    override fun signOut() {
        coroutineScope.launch {
            firebaseAuth.signOut()
        }
    }

    override fun updateName(name: String) {
        coroutineScope.launch {
            firebaseAuth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            )
        }
    }
}