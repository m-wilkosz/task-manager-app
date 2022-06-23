package com.lsm.todo_app.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lsm.todo_app.data.Task
import com.lsm.todo_app.data.TaskDao

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateRoomConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}