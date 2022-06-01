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

    private val _task = MutableLiveData<Task>()
    val task = _task

    override fun prepare(args: Bundle?) {
        super.prepare(args)
        _task.value = Task(title = "", priority = "", category = "", date = Calendar.getInstance().time, time = LocalTime.now(), frequency = "")
    }

    fun saveAddTask() {
        Log.i("saveAddTask", task.value.toString())
    }

    fun showDatePicker() {
        task.value?.let {
            showDatePickerRequest.postValue(it.date)
        }
    }
}