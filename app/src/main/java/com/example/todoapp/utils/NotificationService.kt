package com.example.todoapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar
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