package com.emindev.expensetodolist.main.data.sharedPreference

import android.content.Context
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.helperlibrary.data.sharedpreference.SharedPreferenceRepository
import com.emindev.expensetodolist.main.common.constant.SharedPrefFileNames
import com.emindev.expensetodolist.main.common.constant.SharedPrefPageSettings


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