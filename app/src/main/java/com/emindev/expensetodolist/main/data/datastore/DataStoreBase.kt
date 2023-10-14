package com.emindev.expensetodolist.main.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first

class DataStoreBase(private val context: Context, private val dataStoreName: String) :
    DataStoreInterface {

    private val dataStore = context.createDataStore(dataStoreName)
    override suspend fun save(key: String, value: Int) {
        val dataStoreKey = preferencesKey<Int>(key)
        dataStore.edit { data ->
            data[dataStoreKey] = value
        }
    }

    override suspend fun save(key: String, value: Long) {
        val dataStoreKey = preferencesKey<Long>(key)
        dataStore.edit { data ->
            data[dataStoreKey] = value
        }
    }

    override suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { data ->
            data[dataStoreKey] = value
        }
    }

    override suspend fun save(key: String, value: Float) {
        val dataStoreKey = preferencesKey<Float>(key)
        dataStore.edit { data ->
            data[dataStoreKey] = value
        }
    }

    override suspend fun save(key: String, value: Boolean) {
        val dataStoreKey = preferencesKey<Boolean>(key)
        dataStore.edit { data ->
            data[dataStoreKey] = value
        }
    }

    override suspend fun readInt(key: String): Int? {
        val dataStoreKey = preferencesKey<Int>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override suspend fun readString(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override suspend fun readLong(key: String): Long? {
        val dataStoreKey = preferencesKey<Long>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override suspend fun readFloat(key: String): Float? {
        val dataStoreKey = preferencesKey<Float>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override suspend fun readBoolean(key: String): Boolean? {
        val dataStoreKey = preferencesKey<Boolean>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }


}