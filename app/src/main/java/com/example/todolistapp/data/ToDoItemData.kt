package com.example.todolistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ToDoItemData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "Untitled",
    val desc: String = "",
    val dayMillis: Long? = null,
    val hour: Int? = null,
    val minute: Int? = null,
    val reminderEnabled: Boolean = false,
    val completed: Boolean = false
)
