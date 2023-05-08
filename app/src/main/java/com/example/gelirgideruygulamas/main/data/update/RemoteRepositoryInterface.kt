package com.example.gelirgideruygulamas.main.data.update

import com.example.gelirgideruygulamas.helperlibrary.common.model.Resource
import com.example.gelirgideruygulamas.main.common.util.RemoteData

interface RemoteRepositoryInterface {
    fun remoteData(listener: (Resource<RemoteData>) -> Unit)
}