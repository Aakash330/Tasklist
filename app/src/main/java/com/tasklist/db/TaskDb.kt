package com.tasklist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tasklist.model.TaskTable


@Database( entities = [TaskTable::class], version = 1)
abstract class TaskDb : RoomDatabase() {
   abstract fun getDao():TaskDao

}