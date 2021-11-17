package com.example.tite.presentation

typealias PersonClickListener = (personUID: String) -> Unit
typealias ChatClickListener = (chatId: String, personUID: String) -> Unit
typealias ContactClickListener = (chatId: String, contactUID: String) -> Unit
typealias RelationClickLister = (relation: String) -> Unit