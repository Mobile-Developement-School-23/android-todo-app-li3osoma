package com.example.todoapp.datasource.local_persistence

import androidx.room.TypeConverter
import java.util.UUID

/*

Converts ID

 */
class UUIDConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun uuidFromString(string: String?): UUID {
        return UUID.fromString(string)
    }
}