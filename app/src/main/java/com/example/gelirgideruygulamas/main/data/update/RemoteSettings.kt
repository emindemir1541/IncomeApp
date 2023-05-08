package com.example.gelirgideruygulamas.main.data.update

import android.content.Context
import com.example.gelirgideruygulamas.BuildConfig
import com.example.gelirgideruygulamas.helperlibrary.data.sharedpreference.SharedPreferenceRepository
import com.example.gelirgideruygulamas.main.common.constant.SharedPrefFileNames
import com.example.gelirgideruygulamas.main.common.util.RemoteData
import com.example.gelirgideruygulamas.main.common.constant.SharPrefRemoteSettUtil

class RemoteSettings(private val context: Context) {
    private val repository = SharedPreferenceRepository(context)
    private val currentVersionCode = BuildConfig.VERSION_CODE

    fun setRemoteData(remoteData: RemoteData) {
        repository.setLong(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.VERSION_CODE, remoteData.versionCode)
        repository.setBoolean(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.FORCE_UPDATE, remoteData.forceUpdate)
        repository.setBoolean(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.Lock, remoteData.isLocked)
    }

    fun getRemoteData(): RemoteData {
        return RemoteData(
            repository.getLong(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.VERSION_CODE, currentVersionCode.toLong()),
            repository.getBoolean(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.FORCE_UPDATE, false),
            repository.getBoolean(SharedPrefFileNames.APP_SETTINGS, SharPrefRemoteSettUtil.Lock, false)
        )

    }

}