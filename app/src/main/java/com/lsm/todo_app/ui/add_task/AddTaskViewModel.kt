package com.lsm.todo_app.ui.add_task

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsm.todo_app.data.Task
import com.lsm.todo_app.data.TaskRepository
import com.lsm.todo_app.ui.BaseFragment
import com.lsm.todo_app.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : BaseViewModel() {

    val showDatePickerRequest = BaseFragment.SingleLiveEvent<Date>()
    val showTimePickerRequest = BaseFragment.SingleLiveEvent<Int>()
    val setAlarmRequest = BaseFragment.SingleLiveEvent<Task>()

    private val _task = MutableLiveData<Task>()
    val task = _task

    var id: Long = 0

    override fun prepare(args: Bundle?) {
        super.prepare(args)
        val calendar: Calendar = Calendar.getInstance()
        _task.value = Task(title = "", priority = "", category = "", date = calendar.time,
            hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE), frequency = "")
    }

    fun saveAddTask() {
        Log.i("saveAddTask", task.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            val taskToAdd = task.value
            val cal = Calendar.getInstance()
            cal.setTime(task.value?.date)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            task.value?.date = cal.time
            if (taskToAdd != null) {
                id = taskRepository.insert(taskToAdd)
            }
            setAlarmRequest.postValue(taskToAdd)
        }
    }

    fun showDatePicker() {
        task.value?.let {
            showDatePickerRequest.postValue(it.date)
        }
    }

    fun showTimePicker() {
        task.value?.let {
            showTimePickerRequest.postValue(it.hour)
            showTimePickerRequest.postValue(it.minute)
        }
    }
}