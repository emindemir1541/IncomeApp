package com.example.gelirgideruygulamas.main.data.expense

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExpenseCreator
    val readAllData: LiveData<List<Expense>>

    init {
        val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseCreator(expenseDao)
        readAllData = repository.readAllData
    }

    fun add(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(expense)
        }
    }

    fun updateOne(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateOne(expense)
        }
    }
    fun updateAll(expense: Expense,expenseList: List<Expense>){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateAll(expense,expenseList)
        }
    }

    fun delete(expense: Expense,expenseList: List<Expense>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(expense,expenseList)
        }
    }

    fun readDataByCardId(cardId: Long): LiveData<List<Expense>> = repository.readDataByCardId(cardId)

    fun readSelectedData(context: Context): List<Expense> = repository.readSelectedData(context)

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshData(DateUtil.currentTime)
        }
    }
}

/*  fun deleteAllExpense() {
      viewModelScope.launch(Dispatchers.IO) {
          repository.deleteAll()
      }
  }*/