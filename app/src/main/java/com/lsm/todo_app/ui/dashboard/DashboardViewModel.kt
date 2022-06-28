package com.lsm.todo_app.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.lsm.todo_app.data.TaskRepository
import com.lsm.todo_app.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val taskRepository: TaskRepository) : BaseViewModel() {

    init {
        fetchTaskList()
    }

    private fun fetchTaskList() {
        viewModelScope.launch {
            taskRepository.getAllTasks()
        }
    }
}