package com.example.gelirgideruygulamas.income.data.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncomeViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Income>>
    private val repository: IncomeCreator

    init {
        repository = IncomeCreator(application)
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

    val readSelectedData get() = repository.readSelectedData


}

