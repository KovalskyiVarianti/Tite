package com.example.tite.presentation.messagelist

import androidx.lifecycle.ViewModel
import com.example.tite.data.firebase.repository.FirebaseMessageRepository

class MessageListViewModel(private val messageRepository: FirebaseMessageRepository) : ViewModel() {
}