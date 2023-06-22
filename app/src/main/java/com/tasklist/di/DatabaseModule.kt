package com.tasklist.di

import android.content.Context
import androidx.room.Room
import com.tasklist.db.TaskDb
import com.tasklist.util.AppConstent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideTaskDb(context: Context):TaskDb
    {
        return Room.databaseBuilder(context, TaskDb::class.java, AppConstent.DBNAME).fallbackToDestructiveMigration().build()
    }
}