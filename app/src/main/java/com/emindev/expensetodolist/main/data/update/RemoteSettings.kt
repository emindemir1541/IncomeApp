package com.emindev.expensetodolist.main.data.update

import android.content.Context
import com.emindev.expensetodolist.BuildConfig
import com.emindev.expensetodolist.helperlibrary.data.sharedpreference.SharedPreferenceRepository
import com.emindev.expensetodolist.main.common.util.RemoteData

class RemoteSettings(private val context: Context) {
    private val repository = SharedPreferenceRepository(context)
    private val currentVersionCode = BuildConfig.VERSION_CODE

    fun setRemoteData(remoteData: RemoteData) {
        /*   repository.setLong(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.VERSION_CODE, remoteData.versionCode)
           repository.setBoolean(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.FORCE_UPDATE, remoteData.forceUpdate)
           repository.setBoolean(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.Lock, remoteData.isLocked)*/
    }

    fun getRemoteData(): RemoteData {
        return RemoteData(0, false, false)

        /*RemoteData(
                repository.getLong(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.VERSION_CODE, currentVersionCode.toLong()),
                repository.getBoolean(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.FORCE_UPDATE, false),
                repository.getBoolean(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.Lock, false)
            )*/
    }

}