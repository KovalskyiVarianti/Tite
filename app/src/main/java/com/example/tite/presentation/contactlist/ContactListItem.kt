package com.example.tite.presentation.contactlist

import kotlinx.android.parcel.Parcelize

@Parcelize
sealed interface ContactListItem {
    @Parcelize
    data class ContactItem(
        val contactUID: String,
        val contactName: String,
        val contactPhoto: String,
        val contactEmail: String,
        val relation: String,
        val chatId: String,
    ) : ContactListItem
}
