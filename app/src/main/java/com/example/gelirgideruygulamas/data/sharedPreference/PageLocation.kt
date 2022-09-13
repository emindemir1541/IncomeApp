package com.example.gelirgideruygulamas.data.sharedPreference

import android.content.Context

private const val PAGE_LOCATION = "PAGE_LOCATION"
private const val ID = "ID"

class PageLocation(private val context: Context) {

    fun setValue(value: Int) {
        SharedPreferenceRepository(context).setInt(PAGE_LOCATION, ID, value)
    }

    fun getValue(): Int {
        return SharedPreferenceRepository(context).getInt(PAGE_LOCATION, ID)
    }

}