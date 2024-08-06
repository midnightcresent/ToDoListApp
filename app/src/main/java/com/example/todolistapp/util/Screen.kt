package com.example.todolistapp.util

sealed class Screen(val route: String) {

    data object HomeScreen : Screen(route = "home_screen")
    data object TaskScreen : Screen("task?taskId={taskId}") {
        fun createRoute(taskId: Long? = null) = "task?taskId=${taskId ?: ""}"
    }
}