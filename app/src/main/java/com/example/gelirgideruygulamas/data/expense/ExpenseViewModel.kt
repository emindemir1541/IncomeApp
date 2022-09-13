package com.example.gelirgideruygulamas.data.expense

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gelirgideruygulamas.helper.DateHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExpenseRepository
    val readAllData: LiveData<List<Expense>>
    val readSavedData: LiveData<List<SavedDate>>

    init {
        val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
        readAllData = repository.readAllData
        readSavedData = repository.readSavedData
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(expense)
        }
    }

    fun readSelectedData(context: Context): List<Expense> = repository.readSelectedData(context)

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshData(DateHelper.currentTime)
        }
    }
}

/*  fun deleteAllExpense() {
      viewModelScope.launch(Dispatchers.IO) {
          repository.deleteAll()
      }
  }*/