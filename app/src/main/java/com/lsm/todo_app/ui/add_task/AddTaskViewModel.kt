package com.lsm.todo_app.ui.add_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lsm.todo_app.ui.BaseFragment
import com.lsm.todo_app.ui.BaseViewModel
import java.util.*

class AddTaskViewModel : BaseViewModel() {

    val showDatePickerRequest = BaseFragment.SingleLiveEvent<Date>()

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text*/

    fun showDatePicker() {
        //showDatePickerRequest.postValue()
    }
}