package com.lsm.todo_app.data

import androidx.lifecycle.MutableLiveData
import com.lsm.todo_app.ui.home.Choice
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    val sdf = SimpleDateFormat("EEEE")

    fun insert(task: Task) : Long {

        task.year = task.date.year + 1900
        task.month = task.date.month + 1
        task.day = task.date.date
        task.dayOfWeek = sdf.format(task.date)
        return taskDao.insert(task)
    }

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

        val dayOfWeek: String? = date?.let { sdf.format(it) }
        val year: Int? = choice.value?.date?.year
        val month: Int? = (choice.value?.date?.month)?.plus(1)
        val day: Int? = choice.value?.date?.date

        return when (choice.value?.sortingType) {

            "Priority: High to Low" -> taskDao.getTasksHighToLow(dateString, category, dayOfWeek, day, month, year)
            "Priority: Low to High" -> taskDao.getTasksLowToHigh(dateString, category, dayOfWeek, day, month, year)
            "A to Z" -> taskDao.getTasksAtoZ(dateString, category, dayOfWeek, day, month, year)
            "Z to A" -> taskDao.getTasksZtoA(dateString, category, dayOfWeek, day, month, year)
            "Chronological" -> taskDao.getTasksChrono(dateString, category, dayOfWeek, day, month, year)
            else -> taskDao.getTasksReverseChrono(dateString, category, dayOfWeek, day, month, year)
        }
    }

    fun deleteTask(id: Long) = taskDao.deleteTask(id)

    fun setTaskDone(id: Long) = taskDao.setTaskDone(id)

    fun getTaskById(id: Long) : Task = taskDao.getTaskById(id)
}