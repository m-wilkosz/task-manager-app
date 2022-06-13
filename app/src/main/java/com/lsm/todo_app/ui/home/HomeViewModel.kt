package com.lsm.todo_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lsm.todo_app.data.Task
import com.lsm.todo_app.ui.BaseViewModel
import java.util.*

class HomeViewModel : BaseViewModel() {

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList : LiveData<List<Task>> = _taskList

    init {
        fetchTaskList()
    }

    private fun fetchTaskList() {
        val calendar: Calendar = Calendar.getInstance()
        var newList = listOf(
            Task(title = "", priority = "", category = "", date = calendar.time,
                hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE), frequency = "")
        )
        _taskList.value = newList
    }
}