package com.lsm.todo_app.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lsm.todo_app.data.Task
import com.lsm.todo_app.data.TaskRepository
import com.lsm.todo_app.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val taskRepository: TaskRepository) : BaseViewModel() {

    private var _undoneList : List<Task> = emptyList()
    private var _doneList : List<Task> = emptyList()

    var doneTasksNum = MutableLiveData<Int>()
    var undoneTasksNum = MutableLiveData<Int>()

    init {
        reloadData()
    }

    fun reloadData() {
        fetchUndoneList()
        fetchDoneList()
        countUndone()
        countDone()
    }

    private fun fetchUndoneList() {
        viewModelScope.launch {
            _undoneList = taskRepository.getAllTasks()
        }
    }

    private fun fetchDoneList() {
        viewModelScope.launch {
            _doneList = taskRepository.getAllDoneTasks()
        }
    }

    private fun countUndone() {
        undoneTasksNum.postValue(_undoneList.size)
    }

    private fun countDone() {

        var counter = _doneList.size

        for (item in _undoneList)
            if (item.frequency != "once")
                counter += item.doneCounter

        doneTasksNum.postValue(counter)
    }
}