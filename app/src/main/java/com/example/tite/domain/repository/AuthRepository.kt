package com.example.tite.domain.repository

import com.example.tite.domain.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: MutableStateFlow<AuthState>
    suspend fun logIn(email:String, password:String)
    suspend fun createUser(nickname:String, email: String, password: String)
}