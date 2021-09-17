package com.example.tite.utils

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthUtils {

    val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    val userUID
        get() = firebaseAuth.uid
    val userEmail
        get() = firebaseAuth.currentUser?.email
    val name
        get() = firebaseAuth.currentUser?.displayName
    val photoUrl
        get() = firebaseAuth.currentUser?.photoUrl

    fun updateNameAndPhoto(name: String? = this.name, photoUrl: Uri? = this.photoUrl) =
        firebaseAuth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(photoUrl)
                .build()
        )

    fun loginWithEmailAndPassword(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    fun signUpWithEmailAndPassword(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun checkCredentials(email: String, password: String): CredentialsCheckResult {
        return when {
            email.isBlank() && password.isBlank() -> CredentialsCheckResult.EmptyEmailAndPassword
            email.isBlank() -> CredentialsCheckResult.EmptyEmail
            password.isBlank() -> CredentialsCheckResult.EmptyPassword
            else -> CredentialsCheckResult.Success
        }
    }

    enum class CredentialsCheckResult(val message: String) {
        EmptyEmailAndPassword("Email and password are empty!"),
        EmptyEmail("Email is empty!"),
        EmptyPassword("Password is empty!"),
        Success("Success!")
    }
}