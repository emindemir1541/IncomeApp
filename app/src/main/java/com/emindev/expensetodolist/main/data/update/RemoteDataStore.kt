package com.emindev.expensetodolist.main.data.update

import android.content.Context
import com.emindev.expensetodolist.BuildConfig
import com.emindev.expensetodolist.main.common.helper.addLog
import com.emindev.expensetodolist.main.common.constant.DataStoreNames
import com.emindev.expensetodolist.main.data.datastore.DataStoreBase
import com.emindev.expensetodolist.main.common.model.RemoteModel
import com.emindev.expensetodolist.main.common.constant.RemoteNames
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RemoteDataStore(private val context: Context) {

    private val remoteDataStore = DataStoreBase(context, DataStoreNames.REMOTE)
    private val currentVersionCode = BuildConfig.VERSION_CODE
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        addLog("Remote DataStore Error", data = throwable, description = " get or send data from datastore has error", "RemoteDataStore.kt")
    }

    fun saveRemoteData(remoteModel: RemoteModel) = CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
        remoteDataStore.save(RemoteNames.VERSION_CODE, remoteModel.versionCode)
        remoteDataStore.save(RemoteNames.FORCED_UPDATE, remoteModel.forceUpdate)
        remoteDataStore.save(RemoteNames.LOCK, remoteModel.isLocked)
    }

    fun getRemote() = runBlocking(coroutineExceptionHandler) {
        return@runBlocking RemoteModel(
            versionCode = remoteDataStore.readLong(RemoteNames.VERSION_CODE)?:currentVersionCode.toLong(),
            forceUpdate = remoteDataStore.readBoolean(RemoteNames.FORCED_UPDATE)?:false,
            isLocked = remoteDataStore.readBoolean(RemoteNames.LOCK)?:false
        )
    }

}