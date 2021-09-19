package com.example.tite.domain

import android.net.Uri

interface UserManager {
    val userUID: String?
    val userEmail: String?
    val name: String?
    val photoUrl: Uri?
    val isLoggedIn: Boolean
    fun updateName(name:String)
    fun updatePhoto(uri: Uri?)
    fun signOut()
}