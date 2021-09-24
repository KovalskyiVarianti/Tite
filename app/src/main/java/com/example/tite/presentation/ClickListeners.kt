package com.example.tite.presentation

import com.example.tite.presentation.personlist.PersonListItem

typealias PersonClickListener = (person: PersonListItem.PersonItem) -> Unit
typealias ChatClickListener = (chatId: String, personUID: String) -> Unit