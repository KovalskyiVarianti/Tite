package com.example.tite.presentation.personlist

sealed interface PersonListItem {
    data class PersonItem(
        val personName: String,
        val personImageUrl: String,
        val messageText: String
    ) : PersonListItem
}