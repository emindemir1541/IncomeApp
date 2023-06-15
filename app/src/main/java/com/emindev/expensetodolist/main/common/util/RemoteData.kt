package com.emindev.expensetodolist.main.common.util

import com.emindev.expensetodolist.BuildConfig

data class RemoteData(val versionCode: Long, val forceUpdate: Boolean, val isLocked: Boolean) {
    val hasUpdate = versionCode > BuildConfig.VERSION_CODE
}