package com.example.todolistapp.domain

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.cancelReminder
import com.example.todolistapp.data.ToDoItemData
import com.example.todolistapp.data.ToDoListApi
import com.example.todolistapp.scheduleReminder
import com.example.todolistapp.util.extractDateTimeComponents
import com.example.todolistapp.util.getTimeInMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val toDoListApi: ToDoListApi
): ViewModel() {

    private val _toDoEntriesList = MutableStateFlow<List<ToDoItemData>?>(null)
    val toDoEntriesList = _toDoEntriesList.asStateFlow()

    init {
        keepListUpToDate()
    }

    private fun keepListUpToDate() {
        viewModelScope.launch(Dispatchers.IO) {
            toDoListApi.retrieveToDoList().collect { list ->
                _toDoEntriesList.value = list
            }
        }
    }

    fun upsertTask(toDoItem: ToDoItemData) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoListApi.upsertEntry(toDoItem)
        }
    }

    fun deleteTaskById(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoListApi.deleteEntryById(taskId)
        }
    }

    fun getTaskById(taskId: Long): ToDoItemData? {
        return _toDoEntriesList.value?.find { it.id == taskId }
    }

    fun toggleConfirmedForEntry(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoListApi.toggleConfirmed(id = id)
            toDoListApi.disableReminder(id = id)
        }
    }

    fun updateReminder(context: Context, todo: ToDoItemData) {
        if (todo.reminderEnabled) {
            if (todo.dayMillis != null && todo.hour != null && todo.minute != null) {
                val triple = extractDateTimeComponents(todo.dayMillis)
                scheduleReminder(
                    context,
                    todo.id,
                    todo.title,
                    todo.desc,
                    getTimeInMillis(
                        year = triple.first,
                        month = triple.second,
                        day = triple.third,
                        hour = todo.hour,
                        minute = todo.minute
                    )
                )
            }
        } else {
            cancelReminder(context, todo.id)
        }
    }
}