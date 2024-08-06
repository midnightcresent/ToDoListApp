package com.example.todolistapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class ReminderWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        return try {
            val title = inputData.getString("title")
            val description = inputData.getString("description")
            val requestCode = inputData.getInt("requestCode", 0)

            val intent = Intent(applicationContext, ReminderReceiver::class.java).apply {
                putExtra("title", title)
                putExtra("description", description)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager =
                applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val timeInMillis = inputData.getLong("timeInMillis", System.currentTimeMillis())

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

            Result.success()
        } catch (e: Exception) {
            Log.e("ReminderWorker", "Error setting reminder", e)
            Result.failure()
        }
    }
}

fun canScheduleExactAlarms(context: Context): Boolean {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}

fun requestExactAlarmPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}

fun scheduleReminder(
    context: Context,
    todoId: Long,
    title: String,
    description: String,
    timeInMillis: Long
) {
    if (canScheduleExactAlarms(context)) {
        val data = Data.Builder()
            .putString("title", title)
            .putString("description", description)
            .putLong("timeInMillis", timeInMillis)
            .putInt("requestCode", todoId.toInt())
            .build()

        val reminderWork = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(data)
            .setInitialDelay(timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(reminderWork)
    } else {
        requestExactAlarmPermission(context)
    }
}

fun cancelReminder(context: Context, todoId: Long) {
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        todoId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    pendingIntent.cancel()
}