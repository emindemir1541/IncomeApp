package com.example.gelirgideruygulamas.expense.data.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExpenseCreator
    val readAllData: LiveData<List<Expense>>

    init {
        repository = ExpenseCreator(application)
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

    fun updateAll(expense: Expense, expenseList: List<Expense>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAll(expense, expenseList)
        }
    }

    fun delete(expense: Expense, expenseList: List<Expense>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(expense, expenseList)
        }
    }

    /*fun readDataByCardId(cardId: Long): LiveData<List<Expense>> = repository.readDataByCardId(cardId)*/
    val readSelectedData get() = repository.readSelectedData

}


//fun readSelectedData(context: Context): List<Expense> = repository.readSelectedData(context)

