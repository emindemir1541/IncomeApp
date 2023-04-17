package com.example.gelirgideruygulamas.income.data.room

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncomeViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Income>>
    private val repository: IncomeCreator

    init {
        val incomeDao = IncomeDatabase.getDatabase(application).incomeDao()
        repository = IncomeCreator(incomeDao)
        readAllData = repository.readAllData
    }

    fun add(income: Income) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(income)
        }
    }

    fun updateOne(income: Income){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateOne(income)
        }
    }

    fun updateAll(income: Income,incomeList: List<Income>){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateAll(income,incomeList)
        }
    }

    fun delete(income: Income,incomeList: List<Income> = emptyList()) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(income,incomeList)
        }
    }

    fun readSelectedData(context: Context) = repository.readSelectedData(context)


}

