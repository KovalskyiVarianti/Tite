package com.example.tite.presentation.messagelist

sealed interface MessageListItem {
    data class MessageItem(val messageText: String, val isSelf: Boolean) : MessageListItem
}