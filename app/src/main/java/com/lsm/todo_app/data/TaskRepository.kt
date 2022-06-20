package com.lsm.todo_app.data

import androidx.lifecycle.MutableLiveData
import com.lsm.todo_app.ui.home.Choice
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    fun insert(task: Task) = taskDao.insert(task)

    fun getTasks(choice: MutableLiveData<Choice>): Flow<List<Task>> {

        val date = choice.value?.date
        val cal = Calendar.getInstance()
        if (date != null) {
            cal.time = date
        }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        val date0 = cal.time
        val dateLong : Long = date0.time
        val dateString : String = dateLong.toString()
        return taskDao.getTasks(dateString)
    }
}