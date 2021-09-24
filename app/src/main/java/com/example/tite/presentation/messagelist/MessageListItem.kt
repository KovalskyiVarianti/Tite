package com.example.tite.presentation.messagelist

import kotlinx.android.parcel.Parcelize

@Parcelize
sealed interface MessageListItem {
    @Parcelize
    data class MessageItem(val id: String, val messageText: String, val isSelf: Boolean) :
        MessageListItem
}