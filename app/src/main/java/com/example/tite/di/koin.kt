package com.example.tite.di

import com.example.tite.data.network.RetrofitNotificationApi
import com.example.tite.data.network.firebase.repository.FirebaseAuthRepository
import com.example.tite.presentation.auth.AuthViewModel
import com.example.tite.presentation.messagelist.MessageListViewModel
import com.example.tite.presentation.personlist.PersonListViewModel
import com.example.tite.data.network.firebase.FirebaseUserManager
import com.example.tite.data.network.firebase.database.FirebaseChatDatabase
import com.example.tite.data.network.firebase.database.FirebaseMessageDatabase
import com.example.tite.data.network.firebase.database.FirebasePersonDatabase
import com.example.tite.data.network.firebase.repository.FirebaseChatRepository
import com.example.tite.data.network.firebase.repository.FirebaseMessageRepository
import com.example.tite.data.network.firebase.repository.FirebasePersonRepository
import com.example.tite.domain.*
import com.example.tite.domain.repository.AuthRepository
import com.example.tite.domain.repository.ChatRepository
import com.example.tite.domain.repository.MessageRepository
import com.example.tite.domain.repository.PersonRepository
import com.example.tite.presentation.MainViewModel
import com.example.tite.presentation.chatlist.ChatListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val authModule = module {
    single { FirebaseUserManager(get()) as UserManager }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { MessageListViewModel(get(), get(), get(), get()) }
    viewModel { ChatListViewModel(get(), get(), get()) }
    viewModel { PersonListViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get(), get()) }
}

val databaseModule = module {
    factory { FirebaseChatDatabase(get()) }
    factory { FirebasePersonDatabase(get(), get(), get()) }
    factory { FirebaseMessageDatabase(get()) }
}

val repositoryModule = module {
    factory { FirebaseChatRepository(get()) as ChatRepository }
    factory { FirebasePersonRepository(get()) as PersonRepository }
    factory { FirebaseAuthRepository(get(), get(), get()) as AuthRepository }
    factory { FirebaseMessageRepository(get(), get(), get()) as MessageRepository }
}

val firebaseModule = module {
    single { createRealtimeDB }
    single { createStorage }
    single { createFirebaseAuth }
    single { createFirebaseMessaging }
}

val retrofitModule = module {
    single { createNotificationApiService }
}

private const val FIREBASE_REALTIME_DATABASE_URL =
    "https://tite-f804f-default-rtdb.europe-west1.firebasedatabase.app/"

private val createRealtimeDB: FirebaseDatabase by lazy {
    FirebaseDatabase.getInstance(
        FIREBASE_REALTIME_DATABASE_URL
    )
}
private val createStorage: FirebaseStorage by lazy {
    FirebaseStorage.getInstance()
}
private val createFirebaseAuth: FirebaseAuth by lazy {
    FirebaseAuth.getInstance()
}
private val createFirebaseMessaging: FirebaseMessaging by lazy {
    FirebaseMessaging.getInstance()
}

private val retrofit = Retrofit.Builder()
    .baseUrl(RetrofitNotificationApi.BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

private val createNotificationApiService: RetrofitNotificationApi by lazy {
    retrofit.create(RetrofitNotificationApi::class.java)
}