package com.example.tite.presentation.chatlist

import kotlinx.android.parcel.Parcelize

@Parcelize
sealed interface ChatListItem {
    @Parcelize
    data class ChatItem(
        val id: String,
        val personUID: String,
        val personName: String,
        val personPhoto: String,
        val topMessage: String
    ) : ChatListItem
}