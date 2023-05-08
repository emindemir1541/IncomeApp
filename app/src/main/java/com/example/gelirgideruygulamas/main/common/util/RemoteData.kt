package com.example.gelirgideruygulamas.main.common.util

import com.example.gelirgideruygulamas.BuildConfig

data class RemoteData(val versionCode: Long, val forceUpdate: Boolean, val isLocked: Boolean) {
    val hasUpdate = versionCode > BuildConfig.VERSION_CODE
}