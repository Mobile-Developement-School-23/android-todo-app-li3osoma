package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.App
import com.example.todoapp.ui.view.MainActivity
import com.example.todoapp.ui.view.ToDoItemEditFragment
import com.example.todoapp.ui.view.ToDoListFragment
import com.example.todoapp.ui.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@AppScope
@Component(dependencies = [], modules = [DatabaseModule::class, NetworkModule::class,
    RepositoryModule::class, SharedPreferencesHelperModule::class, AppModule::class])
interface AppComponent {

    fun inject(app: App)
    fun inject(activity: MainActivity)
    fun viewModelFactory(): ViewModelFactory
    fun inject(fragment:ToDoListFragment)
    fun inject(fragment:ToDoItemEditFragment)

//    @Component.Factory
//    interface Factory {
//        fun create(@BindsInstance context: Context): AppComponent
//    }
}