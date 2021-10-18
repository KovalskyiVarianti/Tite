package com.example.tite.di

import com.example.tite.data.firebase.repository.FirebaseAuthRepository
import com.example.tite.presentation.auth.AuthViewModel
import com.example.tite.presentation.messagelist.MessageListViewModel
import com.example.tite.presentation.personlist.PersonListViewModel
import com.example.tite.data.firebase.FirebaseUserManager
import com.example.tite.data.firebase.database.FirebaseChatDatabase
import com.example.tite.data.firebase.database.FirebaseMessageDatabase
import com.example.tite.data.firebase.database.FirebasePersonDatabase
import com.example.tite.data.firebase.repository.FirebaseChatRepository
import com.example.tite.data.firebase.repository.FirebaseMessageRepository
import com.example.tite.data.firebase.repository.FirebasePersonRepository
import com.example.tite.domain.*
import com.example.tite.domain.repository.AuthRepository
import com.example.tite.domain.repository.ChatRepository
import com.example.tite.domain.repository.MessageRepository
import com.example.tite.domain.repository.PersonRepository
import com.example.tite.presentation.chatlist.ChatListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { FirebaseUserManager(createFirebaseAuth) as UserManager }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { MessageListViewModel(get(), get(), get(), get()) }
    viewModel { ChatListViewModel(get(), get(), get()) }
    viewModel { PersonListViewModel(get(), get(), get()) }
}

val databaseModule = module {
    factory { FirebaseChatDatabase(createRealtimeDB) }
    factory { FirebasePersonDatabase(createRealtimeDB, get()) }
    factory { FirebaseMessageDatabase(createRealtimeDB) }
}

val repositoryModule = module {
    factory { FirebaseChatRepository(get()) as ChatRepository }
    factory { FirebasePersonRepository(get()) as PersonRepository }
    factory { FirebaseAuthRepository(get(), createFirebaseAuth, get()) as AuthRepository }
    factory { FirebaseMessageRepository(get()) as MessageRepository }
}

private const val FIREBASE_REALTIME_DATABASE_URL =
    "https://tite-f804f-default-rtdb.europe-west1.firebasedatabase.app/"

val createRealtimeDB: FirebaseDatabase by lazy {
    FirebaseDatabase.getInstance(
        FIREBASE_REALTIME_DATABASE_URL
    )
}
val createFirebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }