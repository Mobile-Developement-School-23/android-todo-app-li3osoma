package com.example.todoapp.ui.view
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.datasource.network.connection.UpdateWorker
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.datasource.persistence.SharedPreferencesHelper
import com.example.todoapp.utils.DAY_THEME
import com.example.todoapp.utils.NIGHT_THEME
import com.example.todoapp.utils.NotificationService
import com.example.todoapp.utils.SYSTEM_THEME
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        setUpTheme()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        periodicUpdate()
    }
    private fun periodicUpdate() {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWorkRequest = PeriodicWorkRequest.Builder(
            UpdateWorker::class.java,
            8,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag("update_data")
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "update_data",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest
        )
    }

    private fun setUpTheme(){
        when (sharedPreferencesHelper.getTheme()){
            SYSTEM_THEME -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            DAY_THEME -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            NIGHT_THEME -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        //setTheme(R.style.Theme_ToDoApp)
    }

}