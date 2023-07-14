package com.example.todoapp.datasource.persistence

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log
import com.example.todoapp.utils.API_PREFERENCES
import com.example.todoapp.utils.DEADLINE_NO
import com.example.todoapp.utils.IS_DEADLINES
import com.example.todoapp.utils.KEY
import com.example.todoapp.utils.SYSTEM_THEME
import com.example.todoapp.utils.THEME
import javax.inject.Inject

/*

Handling API revision

 */
class SharedPreferencesHelper @Inject constructor(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(API_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun putRevision(revision:Int){
        editor.putInt(KEY, revision)
        editor.apply()
    }

    fun getRevision():Int=sharedPreferences.getInt(KEY,0)

    fun putTheme(theme:Int){
        editor.putInt(THEME, theme)
        editor.apply()
    }

    fun getTheme():Int = sharedPreferences.getInt(THEME, SYSTEM_THEME)

    fun updateDeadlineState(state:Boolean){
        editor.putBoolean(IS_DEADLINES, state)
        editor.apply()
    }
    fun getDeadlineState():Boolean = sharedPreferences.getBoolean(IS_DEADLINES, DEADLINE_NO)
}