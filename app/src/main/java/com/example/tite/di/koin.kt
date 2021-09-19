package com.example.tite.di

import com.example.tite.data.firebase.repository.FirebaseAuthRepository
import com.example.tite.presentation.auth.AuthViewModel
import com.example.tite.presentation.messagelist.MessageListViewModel
import com.example.tite.presentation.personlist.PersonListViewModel
import com.example.tite.data.firebase.FirebaseUserManager
import com.example.tite.data.firebase.database.FirebasePersonDatabase
import com.example.tite.data.firebase.repository.FirebaseMessageRepository
import com.example.tite.data.firebase.repository.FirebasePersonRepository
import com.example.tite.domain.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


private const val FIREBASE_REALTIME_DATABASE_URL =
    "https://tite-f804f-default-rtdb.europe-west1.firebasedatabase.app/"

val authModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseUserManager(get()) as UserManager }
    single { FirebaseAuthRepository(get(), get(), get()) }
    viewModel { AuthViewModel(get(), get()) }
}

val personModule = module {
    single { FirebasePersonDatabase(FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL)) }
    single { FirebasePersonRepository(get()) }
    viewModel { PersonListViewModel(get(), get()) }
}

val messageModule = module {
    single { FirebaseMessageRepository() }
    viewModel { MessageListViewModel(get()) }
}