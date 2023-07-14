package com.example.todoapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.todoapp.datasource.persistence.SharedPreferencesHelper
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.AppModule
import com.example.todoapp.di.DaggerAppComponent
import com.example.todoapp.ui.view.MainActivity
import com.example.todoapp.utils.DAY_THEME
import com.example.todoapp.utils.NIGHT_THEME
import com.example.todoapp.utils.SYSTEM_THEME
import javax.inject.Inject

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    "channel_id",
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description =
                        applicationContext.getString(R.string.app_name)
                }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}