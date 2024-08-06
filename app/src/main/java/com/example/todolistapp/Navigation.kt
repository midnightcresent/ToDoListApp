package com.example.todolistapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolistapp.data.ToDoItemData
import com.example.todolistapp.domain.ToDoListViewModel
import com.example.todolistapp.ui.Home
import com.example.todolistapp.ui.Task
import com.example.todolistapp.util.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Navigation(
    navController: NavHostController,
) {
    val viewModel: ToDoListViewModel = hiltViewModel()
    val toDoItems = viewModel.toDoEntriesList
    val context = LocalContext.current
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        homeRoute(
            toDoItems = toDoItems,
            addNewTask = {
                navController.navigate(Screen.TaskScreen.createRoute(0))
            },
            editExistingTask = { item ->
                navController.navigate(Screen.TaskScreen.createRoute(item.id))
            },
            toggleCompleted = { id ->
                viewModel.toggleConfirmedForEntry(id)
            },
            deleteItem = { id ->
                viewModel.deleteTaskById(id)
            }
        )

        taskRoute(
            getTask = { taskId ->
                taskId?.let { viewModel.getTaskById(it) }
            },
            onSave = { item ->
                viewModel.upsertTask(item)
                viewModel.updateReminder(context, item)
                navController.popBackStack()
            },
            onCancel = {
                navController.popBackStack()
            },
        )
    }
}

fun NavGraphBuilder.homeRoute(
    toDoItems: StateFlow<List<ToDoItemData>?>,
    addNewTask: () -> Unit,
    editExistingTask: (ToDoItemData) -> Unit,
    toggleCompleted: (Long) -> Unit,
    deleteItem: (Long) -> Unit,
) {
    composable(route = Screen.HomeScreen.route) {
        val deleteTaskId = it.savedStateHandle.remove<Long>("deleteTaskId")
        deleteTaskId?.let { id ->
            deleteItem(id)
        }
        Home(
            toDoItems = toDoItems,
            addNewTask = addNewTask,
            editExistingTask = editExistingTask,
            toggleCompleted = toggleCompleted,
            deleteItem = deleteItem,
        )
    }
}

fun NavGraphBuilder.taskRoute(
    getTask: (Long?) -> ToDoItemData?,
    onSave: (ToDoItemData) -> Unit,
    onCancel: () -> Unit,
) {
    composable(
        route = Screen.TaskScreen.route,
        arguments = listOf(navArgument("taskId") { type = NavType.LongType })
    ) { backStackEntry ->
        var task = ToDoItemData()
        val taskId = backStackEntry.arguments?.getLong("taskId")
        if (taskId != 0L) {
            task = getTask(taskId)!!
        }
        Task(
            task = task,
            onSave = onSave,
            onCancel = onCancel,
        )
    }
}