package com.emindev.expensetodolist.main.data.update

import android.content.Context
import com.emindev.expensetodolist.main.common.helper.FeedbackUtil.setError
import com.emindev.expensetodolist.main.common.helper.test
import com.emindev.expensetodolist.main.common.model.Resource
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.emindev.expensetodolist.main.common.util.RemoteModel
import com.emindev.expensetodolist.main.common.util.RemoteUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class RemoteRepository(private val context: Context) : RemoteRepositoryInterface {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val configSettings = FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(5).build()


    init {
        remoteConfig.setConfigSettingsAsync(configSettings)
        getRemoteData()
    }

    override fun remoteData(listener: (Resource<RemoteModel>) -> Unit) {
        remoteConfig.fetchAndActivate().addOnSuccessListener {
            val remoteModel = RemoteModel(
                remoteConfig.getLong(RemoteUtil.VERSION_CODE),
                remoteConfig.getBoolean(RemoteUtil.FORCED_UPDATE),
                remoteConfig.getBoolean(RemoteUtil.LOCK)
            )
            listener.invoke(Resource.Success(remoteModel))
        }.addOnFailureListener {
            listener.invoke(Resource.Error(it.localizedMessage))
            setError("Version Check", it, "RemoteRepository/remoteData()", description = "Failed trying to get version code from firebase using FirebaseRemote")
        }
    }


    private val remoteDataStore = RemoteDataStore(context)

    private val _remoteData = MutableStateFlow(remoteDataStore.getRemote())
    val remoteData = _remoteData.asStateFlow()



   private fun getRemoteData() {
        remoteConfig.fetchAndActivate().addOnSuccessListener {
            val newRemoteModel = RemoteModel(
                remoteConfig.getLong(RemoteUtil.VERSION_CODE),
                remoteConfig.getBoolean(RemoteUtil.FORCED_UPDATE),
                remoteConfig.getBoolean(RemoteUtil.LOCK)
            )
            _remoteData.value = newRemoteModel
            remoteDataStore.saveRemoteData(newRemoteModel)
        }.addOnFailureListener {
            setError("Version Check", it, "RemoteRepository/remoteData()", description = "Failed trying to get version code from firebase using FirebaseRemote")
        }
    }
}