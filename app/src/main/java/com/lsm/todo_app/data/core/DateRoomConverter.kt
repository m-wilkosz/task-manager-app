package com.lsm.todo_app.data.core

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

object DateRoomConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let {Date(it)}
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromList(value: List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<String>>(value)

    @TypeConverter
    fun fromIntList(value: List<Int>) = Json.encodeToString(value)

    @TypeConverter
    fun toIntList(value: String) = Json.decodeFromString<List<Int>>(value)
}