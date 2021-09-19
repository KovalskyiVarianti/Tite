package com.example.tite.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tite.data.firebase.repository.FirebaseAuthRepository
import com.example.tite.domain.UserManager
import com.example.tite.presentation.auth.AuthState.Success
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class CredentialsCheckResult(val message: String) {
    EmptyEmailAndPassword("Email and password are empty!"),
    EmptyEmail("Email is empty!"),
    EmptyPassword("Password is empty!"),
    Success("Success!")
}

class AuthViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val userManager: UserManager,
) : ViewModel() {

    private val _loginState =
        MutableStateFlow<AuthState?>(if (userManager.isLoggedIn) Success else null)
    val loginState: StateFlow<AuthState?> = _loginState

    fun login(email: String, password: String) {
        val result = checkCredentials(email, password)
        if (result != CredentialsCheckResult.Success) {
            _loginState.value = AuthState.Failure(result.message)
            return
        }
        viewModelScope.launch {
            firebaseAuthRepository.logIn(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = Success
                } else {
                    _loginState.value = AuthState.Failure(task.exception?.message)
                }
            }
        }
    }

    fun signUp(email: String, password: String) {
        val result = checkCredentials(email, password)
        if (result != CredentialsCheckResult.Success) {
            _loginState.value = AuthState.Failure(result.message)
            return
        }
        viewModelScope.launch {
            firebaseAuthRepository.createUser(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = Success
                } else {
                    _loginState.value = AuthState.Failure(task.exception?.message)
                }
            }
        }

    }

    private fun checkCredentials(email: String, password: String): CredentialsCheckResult {
        return when {
            email.isBlank() && password.isBlank() -> CredentialsCheckResult.EmptyEmailAndPassword
            email.isBlank() -> CredentialsCheckResult.EmptyEmail
            password.isBlank() -> CredentialsCheckResult.EmptyPassword
            else -> CredentialsCheckResult.Success
        }
    }
}