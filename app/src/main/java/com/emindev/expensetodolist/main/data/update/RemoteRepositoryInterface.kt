package com.emindev.expensetodolist.main.data.update

import com.emindev.expensetodolist.main.common.model.Resource
import com.emindev.expensetodolist.main.common.util.RemoteModel

interface RemoteRepositoryInterface {
    fun remoteData(listener: (Resource<RemoteModel>) -> Unit)
}