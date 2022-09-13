package com.example.gelirgideruygulamas.data.income

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gelirgideruygulamas.helper.DateHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncomeViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Income>>
    private val repository: IncomeRepository

    init {
        val incomeDao = IncomeDatabase.getDatabase(application).incomeDao()
        repository = IncomeRepository(incomeDao)
        readAllData = repository.readAllData
    }

    fun addIncome(income: Income) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(income)
        }
    }

    fun deleteIncome(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }

    fun readSelectedData(context: Context) = repository.readSelectedData(context)

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshData(DateHelper.currentTime)
        }
    }

}

/* fun updateIncome(income: Income) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(income)
        }
    }*/

/*fun readDataByDate(dateLong: String): LiveData<List<Income>> = repository.readDataByDate(dateLong)*/

/*   fun deleteAllIncome() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }*/