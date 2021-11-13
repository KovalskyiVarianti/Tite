package com.example.tite.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.tite.domain.UserManager
import com.example.tite.domain.repository.MessageRepository
import com.example.tite.domain.repository.PersonRepository
import com.google.firebase.messaging.FirebaseMessaging

class MainViewModel(
    private val personRepository: PersonRepository,
    private val messageRepository: MessageRepository,
    private val userManager: UserManager,
) : ViewModel() {

    init {
        userManager.userUID?.let {
            personRepository.addPersonInfoListener(it)
            //// firebase test
            messageRepository.subscribeOnNotifications(it)
        }


    }

    val selfPersonState = personRepository.personInfo

    fun uploadAvatar(uri: Uri, onSuccess: (avatarUri : Uri) -> Unit){
        personRepository.uploadAvatar(uri, onSuccess)
    }

    fun signOut() = userManager.signOut()

    override fun onCleared() {
        super.onCleared()
        userManager.userUID?.let { personRepository.removePersonInfoListener(it) }
    }

}