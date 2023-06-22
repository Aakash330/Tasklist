package com.tasklist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TaskTable (
    @PrimaryKey
    val id: Long,
    val taskName:String,
    val time_in_mili:Long,
    val time:String,
    val date:String,
    var status:String
    )
