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

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category ORDER BY CASE Task.priority WHEN 'High' THEN 0 WHEN 'Medium' THEN 1 WHEN 'Low' THEN 2 END")
    fun getTasksHighToLow(date: String, category: String?) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category ORDER BY CASE Task.priority WHEN 'Low' THEN 0 WHEN 'Medium' THEN 1 WHEN 'High' THEN 2 END")
    fun getTasksLowToHigh(date: String, category: String?) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category ORDER BY Task.title ASC")
    fun getTasksAtoZ(date: String, category: String?) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category ORDER BY Task.title DESC")
    fun getTasksZtoA(date: String, category: String?) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category ORDER BY Task.hour ASC, Task.minute ASC")
    fun getTasksChrono(date: String, category: String?) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category ORDER BY Task.hour DESC, Task.minute ASC")
    fun getTasksReverseChrono(date: String, category: String?) : Flow<List<Task>>

    @Query("DELETE FROM Task WHERE Task.id = :id")
    fun deleteTask(id: Long)
}