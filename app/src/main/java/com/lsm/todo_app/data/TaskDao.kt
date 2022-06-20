package com.lsm.todo_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task) : Long

    @Query("SELECT * FROM Task WHERE Task.date == :date")
    fun getTasks(date: String) : Flow<List<Task>>
}