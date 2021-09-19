package com.example.tite.presentation.auth

sealed interface AuthState {
    object Success : AuthState
    data class Failure(val message: String?):AuthState
}