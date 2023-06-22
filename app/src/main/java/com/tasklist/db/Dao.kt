package com.tasklist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tasklist.model.TaskTable

@Dao
interface TaskDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addTask(taskData:TaskTable)

   @Query("SELECT * FROM TaskTable")
   suspend fun getAllTask():List<TaskTable>

   @Query("SELECT * FROM TaskTable where date = :formatDate")
   suspend fun filterData(formatDate:String):List<TaskTable>

   @Query("UPDATE TaskTable SET status = :status WHERE id = :id")
   suspend fun updateStatus(status:String,id:Long):Int
   
   @Query("DELETE FROM TaskTable WHERE id =:id")
   suspend fun deleteTask(id:Long)

   @Query("SELECT time_in_mili FROM TaskTable WHERE time_in_mili < :currenttime ORDER BY time_in_mili ASC ")
   suspend fun filterByAssending(time_in_mili:Long)










}