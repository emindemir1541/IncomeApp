package com.emindev.expensetodolist.main.data.update

import com.emindev.expensetodolist.helperlibrary.common.model.Resource
import com.emindev.expensetodolist.main.common.util.RemoteData

interface RemoteRepositoryInterface {
    fun remoteData(listener: (Resource<RemoteData>) -> Unit)
}