package com.example.todolistapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todolistapp.R
import com.example.todolistapp.data.ToDoItemData
import com.example.todolistapp.util.extractDateTimeComponents
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Task(
    task: ToDoItemData?,
    onSave: (ToDoItemData) -> Unit,
    onCancel: () -> Unit
) {
    if (task != null) {
        var title by remember { mutableStateOf(task.title) }
        var desc by remember { mutableStateOf(task.desc) }
        var dayMillis by remember { mutableStateOf(task.dayMillis) }
        var hour by remember { mutableStateOf(task.hour) }
        var minute by remember { mutableStateOf(task.minute) }
        var reminderEnabled by remember { mutableStateOf(task.reminderEnabled) }
        var completed by remember { mutableStateOf(task.completed) }

        val datePickerState = rememberDatePickerState()

        val currentTime = Calendar.getInstance()

        LaunchedEffect(dayMillis) {
            if (dayMillis == null)
                dayMillis = currentTime.timeInMillis
        }

        val timePickerState =
            if (hour == null || minute == null) {
                rememberTimePickerState(
                    initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
                    initialMinute = currentTime.get(Calendar.MINUTE),
                    is24Hour = true
                )
            } else {
                rememberTimePickerState(
                    initialHour = hour!!,
                    initialMinute = minute!!,
                    is24Hour = true
                )
            }

        var showDateDialog by remember { mutableStateOf(false) }
        var showTimeDialog by remember { mutableStateOf(false) }

        val onDateDismiss: () -> Unit = {
            showDateDialog = false
        }

        val onTimeDismiss: () -> Unit = {
            showTimeDialog = false
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(if (task.id == 0L) "Create Task" else "Edit Task") }
                )
            },
        )
        { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                HorizontalDivider(color = Color.Gray)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (dayMillis != null) {
                                val triple = extractDateTimeComponents(dayMillis!!)
                                Text(text = "${triple.third}/${triple.second}/${triple.first}")
                            } else
                                Text(text = "Not set")
                            IconButton(onClick = { showDateDialog = true }) {
                                Icon(
                                    painterResource(id = R.drawable.calendar_24),
                                    contentDescription = "Change Date"
                                )
                            }
                        }
                    }

                    if (showDateDialog)
                        DatePickerDialog(
                            onDismissRequest = onDateDismiss,
                            confirmButton = {
                                TextButton(onClick = {
                                    dayMillis = datePickerState.selectedDateMillis
                                    onDateDismiss()
                                }) {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = onDateDismiss) {
                                    Text("Cancel")
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (hour != null && minute != null)
                                Text(text = "$hour:$minute")
                            else
                                Text(text = "Not set")
                            IconButton(onClick = { showTimeDialog = true }) {
                                Icon(
                                    painterResource(id = R.drawable.time_24),
                                    contentDescription = "Change Time"
                                )
                            }
                        }
                    }

                    if (showTimeDialog) {
                        AlertDialog(
                            onDismissRequest = onTimeDismiss,
                            title = {
                                Text(text = "Select Time")
                            },
                            text = {
                                TimeInput(
                                    state = timePickerState,
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        hour = timePickerState.hour
                                        minute = timePickerState.minute
                                        onTimeDismiss()
                                    }
                                ) {
                                    Text("Confirm selection")
                                }
                            },
                            dismissButton = {
                                Button(onClick = onTimeDismiss) {
                                    Text("Dismiss picker")
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Reminder Enabled")
                        Switch(
                            checked = reminderEnabled,
                            onCheckedChange = {
                                if (reminderEnabled)
                                    reminderEnabled = it
                                else if (!completed && hour != null && minute != null)
                                    reminderEnabled = it
                            }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Completed")
                        Switch(
                            checked = completed,
                            onCheckedChange = {
                                reminderEnabled = false
                                completed = it
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = onCancel) {
                            Text("Cancel")
                        }

                        Button(onClick = {
                            val updatedTask = ToDoItemData(
                                id = task.id,
                                title = title,
                                desc = desc,
                                dayMillis = dayMillis ?: task.dayMillis,
                                hour = hour ?: task.hour,
                                minute = minute ?: task.minute,
                                reminderEnabled = reminderEnabled,
                                completed = completed
                            )
                            onSave(updatedTask)
                        }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}