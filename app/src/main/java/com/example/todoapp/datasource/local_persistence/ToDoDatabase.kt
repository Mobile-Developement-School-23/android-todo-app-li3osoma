package com.example.todoapp.datasource.local_persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.domain.model.ToDoItem
/*

Database

*/
@Database(entities = [ToDoItem::class], version = 1)
@TypeConverters(UUIDConverter::class)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun dao(): ToDoDao
}