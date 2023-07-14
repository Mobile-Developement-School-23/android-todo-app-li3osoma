package com.example.todoapp.utils

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.todoapp.R
import com.example.todoapp.datasource.repository.ToDoRepositoryImpl
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.repository.ToDoRepository
import com.example.todoapp.ui.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class NotificationService @Inject constructor(
    private val context: Context,
){
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    private val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, AppReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun scheduleAlarm() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelAlarm() {
        alarmManager.cancel(pendingIntent)
    }
}