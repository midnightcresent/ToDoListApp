package com.example.todolistapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoListApi {

    @Upsert
    suspend fun upsertEntry(toDoListItemData: ToDoItemData)

    @Query("DELETE FROM ToDoItemData WHERE id = :itemId")
    suspend fun deleteEntryById(itemId: Long)

    @Query("SELECT * FROM ToDoItemData")
    fun retrieveToDoList(): Flow<List<ToDoItemData>>

    @Query("UPDATE ToDoItemData SET completed = NOT completed WHERE id = :id")
    suspend fun toggleConfirmed(id: Long)

    @Query("UPDATE ToDoItemData SET reminderEnabled = 0 WHERE id = :id")
    suspend fun disableReminder(id: Long)
}