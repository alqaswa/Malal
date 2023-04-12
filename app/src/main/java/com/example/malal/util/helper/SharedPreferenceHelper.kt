package com.example.malal.util.helper

import android.content.SharedPreferences
import android.util.Log
import com.example.malal.util.FIRST_LOGGED_IN_APP
import javax.inject.Inject

class SharedPreferenceHelper
@Inject
constructor(private val sharedPref:SharedPreferences)
{
    private fun getBooleanValue(): Boolean
    {
        return sharedPref.getBoolean(FIRST_LOGGED_IN_APP, true)
    }

    private fun changeBooleanValueToFalse()
    {
        val editor = sharedPref.edit()
        editor.putBoolean(FIRST_LOGGED_IN_APP, false).apply()
    }

    fun isFirstTimeOpened(): Boolean
    {
        val isFirstLogApp = getBooleanValue()
        if (isFirstLogApp)
            changeBooleanValueToFalse()
        return isFirstLogApp
    }
}