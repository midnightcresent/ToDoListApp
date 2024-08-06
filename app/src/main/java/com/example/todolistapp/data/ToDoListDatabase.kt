package com.example.todolistapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ToDoItemData::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ToDoListDatabase : RoomDatabase() {

    abstract val toDoListApi: ToDoListApi
}