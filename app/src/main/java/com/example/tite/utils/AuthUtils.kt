package com.example.tite.utils

import androidx.navigation.fragment.findNavController
import com.example.tite.presentation.auth.LoginFragmentDirections
import com.google.firebase.auth.FirebaseAuth

fun loginWithEmailAndPassword(email: String, password: String) =
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)

fun checkCredentials(email: String, password: String): CredentialsCheckResult {
    return when {
        email.isBlank() && password.isBlank() -> CredentialsCheckResult.EmptyEmailAndPassword
        email.isBlank() -> CredentialsCheckResult.EmptyEmail
        password.isBlank() -> CredentialsCheckResult.EmptyPassword
        else -> CredentialsCheckResult.Success
    }
}

enum class CredentialsCheckResult(val message: String){
    EmptyEmailAndPassword("Email and password are empty!"),
    EmptyEmail("Email is empty!"),
    EmptyPassword("Password is empty!"),
    Success("Success!")

}