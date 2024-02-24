package com.example.githubusersearch

import android.app.Application
import com.example.githubusersearch.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GithubUserSearchApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GithubUserSearchApplication)
            modules(listOf(appModule))
        }
    }

}