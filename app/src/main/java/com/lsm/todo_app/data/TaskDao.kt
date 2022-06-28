package com.lsm.todo_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task) : Long

    @Query("SELECT *, CASE Task.priority WHEN 'High' THEN 0 WHEN 'Medium' THEN 1 WHEN 'Low' THEN 2 END AS [order] FROM Task WHERE Task.date == :date AND Task.category LIKE :category AND Task.status LIKE 'undone' UNION SELECT *, CASE Task.priority WHEN 'High' THEN 0 WHEN 'Medium' THEN 1 WHEN 'Low' THEN 2 END AS [order] FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'daily' AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT *, CASE Task.priority WHEN 'High' THEN 0 WHEN 'Medium' THEN 1 WHEN 'Low' THEN 2 END AS [order] FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'weekly' AND Task.dayOfWeek LIKE :dayOfWeek AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT *, CASE Task.priority WHEN 'High' THEN 0 WHEN 'Medium' THEN 1 WHEN 'Low' THEN 2 END AS [order] FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'monthly' AND Task.day = :day AND :year >= Task.year AND :month >= Task.month UNION SELECT *, CASE Task.priority WHEN 'High' THEN 0 WHEN 'Medium' THEN 1 WHEN 'Low' THEN 2 END AS [order] FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'yearly' AND Task.month = :month AND Task.day = :day AND :year >= Task.year ORDER BY [order]")
    fun getTasksHighToLow(date: String, category: String?, dayOfWeek: String?, day: Int?, month: Int?, year: Int?) : Flow<List<Task>>

    @Query("SELECT *, CASE Task.priority WHEN 'Low' THEN 0 WHEN 'Medium' THEN 1 WHEN 'High' THEN 2 END AS [order] FROM Task WHERE Task.date == :date AND Task.category LIKE :category AND Task.status LIKE 'undone' UNION SELECT *, CASE Task.priority WHEN 'Low' THEN 0 WHEN 'Medium' THEN 1 WHEN 'High' THEN 2 END AS [order] FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'daily' AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT *, CASE Task.priority WHEN 'Low' THEN 0 WHEN 'Medium' THEN 1 WHEN 'High' THEN 2 END AS [order] FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'weekly' AND Task.dayOfWeek LIKE :dayOfWeek AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT *, CASE Task.priority WHEN 'Low' THEN 0 WHEN 'Medium' THEN 1 WHEN 'High' THEN 2 END AS [order] FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'monthly' AND Task.day = :day AND :year >= Task.year AND :month >= Task.month UNION SELECT *, CASE Task.priority WHEN 'Low' THEN 0 WHEN 'Medium' THEN 1 WHEN 'High' THEN 2 END AS [order] FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'yearly' AND Task.month = :month AND Task.day = :day AND :year >= Task.year ORDER BY [order]")
    fun getTasksLowToHigh(date: String, category: String?, dayOfWeek: String?, day: Int?, month: Int?, year: Int?) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category AND Task.status LIKE 'undone' UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'daily' AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'weekly' AND Task.dayOfWeek LIKE :dayOfWeek AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'monthly' AND Task.day = :day AND :year >= Task.year AND :month >= Task.month UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'yearly' AND Task.month = :month AND Task.day = :day AND :year >= Task.year ORDER BY Task.title ASC")
    fun getTasksAtoZ(date: String, category: String?, dayOfWeek: String?, day: Int?, month: Int?, year: Int?) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category AND Task.status LIKE 'undone' UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'daily' AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'weekly' AND Task.dayOfWeek LIKE :dayOfWeek AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'monthly' AND Task.day = :day AND :year >= Task.year AND :month >= Task.month UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'yearly' AND Task.month = :month AND Task.day = :day AND :year >= Task.year ORDER BY Task.title DESC")
    fun getTasksZtoA(date: String, category: String?, dayOfWeek: String?, day: Int?, month: Int?, year: Int?) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category AND Task.status LIKE 'undone' UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'daily' AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'weekly' AND Task.dayOfWeek LIKE :dayOfWeek AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'monthly' AND Task.day = :day AND :year >= Task.year AND :month >= Task.month UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'yearly' AND Task.month = :month AND Task.day = :day AND :year >= Task.year ORDER BY Task.hour ASC, Task.minute ASC")
    fun getTasksChrono(date: String, category: String?, dayOfWeek: String?, day: Int?, month: Int?, year: Int?) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE Task.date == :date AND Task.category LIKE :category AND Task.status LIKE 'undone' UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'daily' AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'weekly' AND Task.dayOfWeek LIKE :dayOfWeek AND :year >= Task.year AND :month >= Task.month AND :day >= Task.day UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'monthly' AND Task.day = :day AND :year >= Task.year AND :month >= Task.month UNION SELECT * FROM Task WHERE Task.category LIKE :category AND Task.status LIKE 'undone' AND Task.frequency LIKE 'yearly' AND Task.month = :month AND Task.day = :day AND :year >= Task.year ORDER BY Task.hour DESC, Task.minute ASC")
    fun getTasksReverseChrono(date: String, category: String?, dayOfWeek: String?, day: Int?, month: Int?, year: Int?) : Flow<List<Task>>

    @Query("DELETE FROM Task WHERE Task.id = :id")
    fun deleteTask(id: Long)

    @Query("UPDATE Task SET status = 'done' WHERE Task.id == :id")
    fun setTaskDone(id: Long)

    @Query("SELECT * FROM Task WHERE Task.id == :id")
    fun getTaskById(id: Long) : Task

    @Query("UPDATE Task SET date = :date, year = :year, month = :month, day = :day, doneCounter = doneCounter + 1 WHERE Task.id == :id")
    fun updateDate(date: Date, id: Long, year: Int, month: Int, day: Int)

    @Query("SELECT * FROM Task WHERE Task.status LIKE 'undone'")
    fun getAllTasks() : List<Task>
}