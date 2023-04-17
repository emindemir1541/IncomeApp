package com.example.gelirgideruygulamas.main.data.sharedPreference

import android.content.Context
import com.example.gelirgideruygulamas.main.common.constant.SharedPrefFileNames
import com.example.gelirgideruygulamas.main.common.constant.SharedPrefPageLocation


class PageLocation(private val context: Context) {

    fun setValue(value: Int) {
        SharedPreferenceRepository(context).setInt(SharedPrefFileNames.PAGE_LOCATION, SharedPrefPageLocation.LOCATION_KEY, value)
    }

    fun getValue(): Int {
        return SharedPreferenceRepository(context).getInt(SharedPrefFileNames.PAGE_LOCATION, SharedPrefPageLocation.LOCATION_KEY)
    }

}