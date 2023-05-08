package com.example.gelirgideruygulamas.main.data.sharedPreference

import android.content.Context
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.helperlibrary.data.sharedpreference.SharedPreferenceRepository
import com.example.gelirgideruygulamas.main.common.constant.SharedPrefFileNames
import com.example.gelirgideruygulamas.main.common.constant.SharedPrefPageSettings


class PageSettings(context: Context) {

    private val repository = SharedPreferenceRepository(context)

    fun setPageLocation(value: Int) {
        repository.setInt(SharedPrefFileNames.PAGE_SETTINGS, SharedPrefPageSettings.PAGE_LOCATION, value)
    }

    val pageLocation: Int
        get() {
            return repository.getInt(SharedPrefFileNames.PAGE_SETTINGS, SharedPrefPageSettings.PAGE_LOCATION,R.id.itemHome)
        }

}