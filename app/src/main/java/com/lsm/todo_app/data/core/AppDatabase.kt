package com.lsm.todo_app.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lsm.todo_app.data.TaskDao

@Database(
    entities = [],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}