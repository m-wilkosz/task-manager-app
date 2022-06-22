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

        val category : String? = if (choice.value?.category == "All") "%" else choice.value?.category

        return when (choice.value?.sortingType) {

            "Priority: High to Low" -> taskDao.getTasksHighToLow(dateString, category)
            "Priority: Low to High" -> taskDao.getTasksLowToHigh(dateString, category)
            "A to Z" -> taskDao.getTasksAtoZ(dateString, category)
            "Z to A" -> taskDao.getTasksZtoA(dateString, category)
            "Chronological" -> taskDao.getTasksChrono(dateString, category)
            else -> taskDao.getTasksReverseChrono(dateString, category)
        }
    }

    fun deleteTask(id: Long) = taskDao.deleteTask(id)

    fun setTaskDone(id: Long) = taskDao.setTaskDone(id)
}