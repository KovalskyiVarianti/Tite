package com.example.tite

import android.app.Application
import com.example.tite.di.authModule
import com.example.tite.di.messageModule
import com.example.tite.di.personModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initKoin()
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@App)
            modules(authModule, personModule, messageModule)
        }
    }
}