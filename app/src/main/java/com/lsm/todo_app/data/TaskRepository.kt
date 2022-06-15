package com.lsm.todo_app.data

import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    fun insert(task: Task) = taskDao.insert(task)
    fun getTasks() = taskDao.getTasks()
}