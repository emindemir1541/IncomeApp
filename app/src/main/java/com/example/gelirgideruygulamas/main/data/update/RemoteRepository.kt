package com.example.gelirgideruygulamas.main.data.update

import com.example.gelirgideruygulamas.helperlibrary.common.helper.FeedbackUtil.setError
import com.example.gelirgideruygulamas.helperlibrary.common.model.Resource
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.example.gelirgideruygulamas.main.common.util.RemoteData
import com.example.gelirgideruygulamas.main.common.util.RemoteUtil


class RemoteRepository() : RemoteRepositoryInterface {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val configSettings = FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(5).build()

    init {
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    override fun remoteData(listener: (Resource<RemoteData>) -> Unit) {
        remoteConfig.fetchAndActivate().addOnSuccessListener {
            val remoteData = RemoteData(
                remoteConfig.getLong(RemoteUtil.VERSION_CODE),
                remoteConfig.getBoolean(RemoteUtil.FORCED_UPDATE),
                remoteConfig.getBoolean(RemoteUtil.LOCK)
            )
            listener.invoke(Resource.Success(remoteData))
        }.addOnFailureListener {
            listener.invoke(Resource.Error(it.localizedMessage))
            setError("Version Check", it, "RemoteRepository/remoteData()", description = "Failed trying to get version code from firebase using FirebaseRemote")
        }
    }
}