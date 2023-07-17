package com.example.todoapp.di

import com.example.todoapp.App
import com.example.todoapp.ui.common.MainActivity
import com.example.todoapp.ui.edit_screen.ToDoItemEditFragment
import com.example.todoapp.ui.list_screen.ToDoListFragment
import com.example.todoapp.ui.common.ViewModelFactory
import dagger.Component

@AppScope
@Component(dependencies = [], modules = [DatabaseModule::class, NetworkModule::class,
    RepositoryModule::class, SharedPreferencesHelperModule::class, AppModule::class])
interface AppComponent {

    fun inject(app: App)
    fun inject(activity: MainActivity)
    fun viewModelFactory(): ViewModelFactory
    fun inject(fragment: ToDoListFragment)
    fun inject(fragment: ToDoItemEditFragment)

//    @Component.Factory
//    interface Factory {
//        fun create(@BindsInstance context: Context): AppComponent
//    }
}