package com.example.gelirgideruygulamas.helperlibrary.data.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceBase(private val context: Context) {

    fun preferences(fileName: String): SharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun editor(fileName: String): SharedPreferences.Editor = preferences(fileName).edit()

}