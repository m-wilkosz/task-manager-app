package com.lsm.todo_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    var title: String = "",
    var priority: String = "",
    var category: String = "",
    var date: Date,
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var dayOfWeek: String = "",
    var hour: Int = 0,
    var minute: Int = 0,
    var frequency: String = "",
    var status: String = "undone"
)