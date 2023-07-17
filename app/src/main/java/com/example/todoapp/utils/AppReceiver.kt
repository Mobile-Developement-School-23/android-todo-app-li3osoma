package com.example.todoapp.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.todoapp.R
import com.example.todoapp.datasource.repository.ToDoRepositoryImpl
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.ui.common.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

private const val SECONDS_DAY: Long = 24 * 60 * 60

class AppReceiver: BroadcastReceiver(){

    @Inject
    lateinit var toDoRepositoryImpl:ToDoRepositoryImpl


    override fun onReceive(context: Context?, intent: Intent?) {
        val intent1 = Intent(context, NotificationService::class.java)
        context!!.startService(intent1)
        if(isDeadline()){
            showNotification(context)
        }

    }

    fun showNotification(context: Context) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context, 1, activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_IMMUTABLE else 0
        )
        val incrementIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, AppReceiver::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_IMMUTABLE else 0
        )
        val notification = NotificationCompat.Builder(context, DEADLINE_CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_save)
            .setContentTitle("Hurry up!")
            .setContentText("You have som tasks to complete for today")
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }

    fun isDeadline():Boolean{
        lateinit var list:List<ToDoItem>
        CoroutineScope(Dispatchers.IO).launch {
            list = toDoRepositoryImpl.getListDb().toList()[0]
        }
        return list.any { toDoItem: ToDoItem -> toDoItem.deadline!! - Date().time< SECONDS_DAY && !toDoItem.done}
    }
    companion object{
        const val DEADLINE_CHANNEL_ID = "deadline_channel"
    }
}