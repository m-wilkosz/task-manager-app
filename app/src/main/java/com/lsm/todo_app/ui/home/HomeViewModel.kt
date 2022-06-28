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

    val cancelAlarmRequest = BaseFragment.SingleLiveEvent<Long>()

    init {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()
        cal[Calendar.YEAR] = cal.time.year + 1900
        cal[Calendar.MONTH] = cal.time.month
        cal[Calendar.DAY_OF_MONTH] = cal.time.date
        cal[Calendar.HOUR_OF_DAY] = 0
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val date = cal.time
        _choice.postValue(Choice(date, "All", "Chronological"))
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
        cancelAlarmRequest.postValue(id)
        fetchTaskList()
    }

    fun setTaskDone(id: Long) {
        viewModelScope.launch {
            val taskToModify: Task = taskRepository.getTaskById(id)
            when (taskToModify.frequency) {
                "once" -> taskRepository.setTaskDone(id)
                "daily" -> taskToModify.day += 1
                "weekly" -> {
                    when (taskToModify.month) {
                        1, 3, 5, 7, 8, 10, 12 -> {
                            val newDay = (taskToModify.day + 7) % 31
                            if (newDay < taskToModify.day) {
                                taskToModify.month += 1
                            }
                            taskToModify.day = newDay
                        }
                        4, 6, 9, 11 -> {
                            val newDay = (taskToModify.day + 7) % 30
                            if (newDay < taskToModify.day) {
                                taskToModify.month += 1
                            }
                            taskToModify.day = newDay
                        }
                        else -> {
                            val newDay = (taskToModify.day + 7) % 28
                            if (newDay < taskToModify.day) {
                                taskToModify.month += 1
                            }
                            taskToModify.day = newDay
                        }
                    }
                }
                "monthly" -> taskToModify.month = (taskToModify.month + 1) % 12
                "yearly" -> taskToModify.year += 1
            }
            val cal = Calendar.getInstance()
            cal[Calendar.YEAR] = taskToModify.year
            cal[Calendar.MONTH] = taskToModify.month - 1
            cal[Calendar.DAY_OF_MONTH] = taskToModify.day
            cal[Calendar.HOUR_OF_DAY] = 0
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val date = cal.time
            taskRepository.updateDate(date, id, taskToModify.year, taskToModify.month, taskToModify.day)
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