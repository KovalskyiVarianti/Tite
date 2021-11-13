package com.example.tite.presentation.personlist

import kotlinx.android.parcel.Parcelize

@Parcelize
sealed interface PersonListItem {
    @Parcelize
    data class PersonItem(
        val uid: String,
        val personName: String,
        val personEmail: String,
        val personImageUri: String,
    ) : PersonListItem
}