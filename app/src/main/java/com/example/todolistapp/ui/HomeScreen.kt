package com.example.todolistapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.todolistapp.R
import com.example.todolistapp.data.ToDoItemData
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    toDoItems: StateFlow<List<ToDoItemData>?>,
    addNewTask: () -> Unit,
    editExistingTask: (ToDoItemData) -> Unit,
    toggleCompleted: (Long) -> Unit,
    deleteItem: (Long) -> Unit
) {
    val toDoItemsThing by toDoItems.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = addNewTask) {
                Icon(painter = painterResource(id = R.drawable.add_24), contentDescription = "Add button")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            HorizontalDivider(color = Color.Gray)
            if (toDoItemsThing != null) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    items(toDoItemsThing!!) { item ->
                        Spacer(modifier = Modifier.height(8.dp))
                        ToDoItem(
                            item = item,
                            editExistingTask = editExistingTask,
                            toggleCompleted = toggleCompleted,
                            deleteItem = deleteItem
                        )
                    }
                }
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    text = "Add a task!",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}