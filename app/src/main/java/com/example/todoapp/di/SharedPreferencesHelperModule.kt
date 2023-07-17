package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.datasource.local_persistence.SharedPreferencesHelper
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesHelperModule {
    @Provides
    @AppScope
    fun provideSharedPreferences(context: Context): SharedPreferencesHelper =
        SharedPreferencesHelper(context)
}