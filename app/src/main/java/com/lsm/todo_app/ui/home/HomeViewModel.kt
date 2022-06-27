package com.lsm.todo_app.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lsm.todo_app.data.Task
import com.lsm.todo_app.data.TaskRepository
import com.lsm.todo_app.ui.BaseFragment
import com.lsm.todo_app.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val taskRepository: TaskRepository) : BaseViewModel() {

    val showDatePickerRequest = BaseFragment.SingleLiveEvent<Date>()
    val showTaskDescriptionRequest = BaseFragment.SingleLiveEvent<Long>()

    private val _taskList = MutableLiveData<List<Task>>()
    var taskList = _taskList

    private val _choice = MutableLiveData<Choice>()
    var choice = _choice

    private val _task = MutableLiveData<Task>()
    var task = _task

    init {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        _choice.postValue(Choice(Date(year, month, day), "All", "Chronological"))
        fetchTaskList()
    }

    private fun fetchTaskList() {
        viewModelScope.launch {
            taskRepository.getTasks(choice).collect { list ->
                _taskList.postValue(list)
            }
        }
    }

    fun showDatePicker() {
        choice.value?.let {
            showDatePickerRequest.postValue(it.date)
        }
    }

    fun applyChoice() {
        fetchTaskList()
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            taskRepository.deleteTask(id)
        }
        fetchTaskList()
    }

    fun setTaskDone(id: Long) {
        viewModelScope.launch {
            taskRepository.setTaskDone(id)
        }
        fetchTaskList()
    }

    fun showTaskDescription(id: Long) {
        showTaskDescriptionRequest.postValue(id)
    }

    fun fetchTask(id: Long) {
        viewModelScope.launch {
            _task.postValue(taskRepository.getTaskById(id))
        }
    }
}