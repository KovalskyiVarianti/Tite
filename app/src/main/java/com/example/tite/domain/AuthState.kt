package com.example.tite.domain

sealed interface AuthState {
    object LoggedIn : AuthState
    object LoggedOut : AuthState
    data class Failure(val message: String) : AuthState
}
