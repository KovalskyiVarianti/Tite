package com.example.tite.di

import com.example.tite.presentation.auth.AuthViewModel
import com.example.tite.presentation.messagelist.MessageListViewModel
import com.example.tite.presentation.personlist.PersonListViewModel
import com.example.tite.utils.AuthUtils
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { AuthViewModel() }
    viewModel { PersonListViewModel() }
    viewModel { MessageListViewModel() }
    single { AuthUtils() }
}