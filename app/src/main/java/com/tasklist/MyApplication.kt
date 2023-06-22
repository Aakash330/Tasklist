package com.tasklist

import android.app.Application
import com.tasklist.di.ApplicationComponent
import com.tasklist.di.DaggerApplicationComponent

class MyApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        applicationComponent= DaggerApplicationComponent.factory().create(this)
    }
}