package com.emindev.expensetodolist.main.data.retrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.expensetodolist.main.common.model.Resource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class CurrencyViewModel() : ViewModel() {


    val currency = flow {
        // TODO:  kotlinx.coroutines.withTimeoutOrNull()
        emit(Resource.Loading())
        val resource = RetrofitInstance.api.getCurrency()
        if (resource.body() == null)
            emit(Resource.Error("Data is null"))
        else if (!resource.isSuccessful)
            emit(Resource.Error(resource.message()))
        else
            emit(Resource.Success(resource.body()?.result))

    }.catch { e ->
        emit(Resource.Error(e.localizedMessage))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),Resource.Loading())
}
