package com.lsm.todo_app.ui.home

import androidx.lifecycle.LiveData
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

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList : LiveData<List<Task>> = _taskList

    private val _choice = MutableLiveData<Choice>()
    val choice = _choice

    init {
        fetchTaskList()
        choice.postValue(Choice())
    }

    private fun fetchTaskList() {
        viewModelScope.launch {
            taskRepository.getTasks().collect { list ->
                _taskList.postValue(list)
            }
        }

//        val calendar: Calendar = Calendar.getInstance()
//        var newList = listOf(
//            Task(title = "", priority = "", category = "", date = calendar.time,
//                hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE), frequency = "")
//        )
//        _taskList.value = newList
    }

    fun showDatePicker() {
        choice.value?.let {
            showDatePickerRequest.postValue(it.date)
        }
    }
}