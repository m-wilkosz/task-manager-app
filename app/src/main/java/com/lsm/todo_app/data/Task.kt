package com.lsm.todo_app.data

import java.time.LocalTime
import java.util.*

data class Task (
    var title: String = "",
    var priority: String = "",
    var category: String = "",
    var date: Date,
    var time: LocalTime,
    var frequency: String = ""
)