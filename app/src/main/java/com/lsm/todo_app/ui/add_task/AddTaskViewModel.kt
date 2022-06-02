package com.lsm.todo_app.ui.add_task

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lsm.todo_app.data.Task
import com.lsm.todo_app.ui.BaseFragment
import com.lsm.todo_app.ui.BaseViewModel
import java.time.LocalTime
import java.util.*

class AddTaskViewModel : BaseViewModel() {

    val showDatePickerRequest = BaseFragment.SingleLiveEvent<Date>()
    val showTimePickerRequest = BaseFragment.SingleLiveEvent<Int>()

    private val _task = MutableLiveData<Task>()
    val task = _task

    override fun prepare(args: Bundle?) {
        super.prepare(args)
        val calendar: Calendar = Calendar.getInstance()
        _task.value = Task(title = "", priority = "", category = "", date = calendar.time,
            hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE), frequency = "")
    }

    fun saveAddTask() {
        Log.i("saveAddTask", task.value.toString())
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