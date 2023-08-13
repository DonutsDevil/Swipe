package com.swapnil.myapplication.views

import android.app.Application
import com.swapnil.myapplication.di.ApplicationComponent
import com.swapnil.myapplication.di.DaggerApplicationComponent

class SwipeApplication : Application() {
    lateinit var component: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder().build()
    }
}