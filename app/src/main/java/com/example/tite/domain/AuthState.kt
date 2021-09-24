package com.example.tite.domain

sealed interface AuthState {
    object LoggedIn : AuthState
    object LoggedOut : AuthState
    data class Failure(val message: String) : AuthState {
        //Workaround for state flow triggering
        override fun equals(other: Any?): Boolean {
            return false
        }

        override fun hashCode(): Int {
            return message.hashCode()
        }
    }
}
