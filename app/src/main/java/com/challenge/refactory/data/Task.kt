package com.challenge.refactory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "task_name")
    val name: String,
    @ColumnInfo(name = "task_desc")
    val description: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "start_time")
    val startTime: String,
    @ColumnInfo(name = "finish_time")
    val finishTime: String,
)