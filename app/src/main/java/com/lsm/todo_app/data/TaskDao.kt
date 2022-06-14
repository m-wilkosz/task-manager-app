package com.lsm.todo_app.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task) : Long

    @Query("SELECT * FROM Task")
    fun getTask() : Flow<List<Task>>
}