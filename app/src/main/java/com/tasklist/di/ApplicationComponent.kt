package com.tasklist.di

import android.content.Context
import com.tasklist.db.TaskDb
import com.tasklist.ui.activity.AddTask
import com.tasklist.ui.activity.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface ApplicationComponent {
    /**
     * method return the Dao object
     */
    fun getTaskDB(): TaskDb
    fun inject(taskActivity: AddTask)
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context):ApplicationComponent
    }
}