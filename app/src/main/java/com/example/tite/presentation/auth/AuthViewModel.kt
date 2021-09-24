package com.example.tite.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tite.domain.repository.AuthRepository
import com.example.tite.domain.AuthState
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authState = authRepository.authState
    val authState = _authState.asStateFlow()

    fun login(email: String, password: String) {
        val result = checkCredentials(email, password)
        if (result != CredentialsCheckResult.Success) {
            _authState.value = AuthState.Failure(result.message)
            return
        }
        viewModelScope.launch {
            authRepository.logIn(email, password)
        }
    }


    fun signUp(nickname: String, email: String, password: String) {
        val result = checkCredentials(email, password)
        if (result != CredentialsCheckResult.Success) {
            _authState.value = AuthState.Failure(result.message)
            return
        }
        viewModelScope.launch {
            authRepository.createUser(nickname, email, password)
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

    enum class CredentialsCheckResult(val message: String) {
        EmptyEmailAndPassword("Email and password are empty!"),
        EmptyEmail("Email is empty!"),
        EmptyPassword("Password is empty!"),
        Success("Success!")
    }
}