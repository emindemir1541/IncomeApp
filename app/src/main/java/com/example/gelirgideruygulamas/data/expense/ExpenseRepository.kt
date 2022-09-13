package com.example.gelirgideruygulamas.data.expense

import android.content.Context
import androidx.lifecycle.LiveData

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    fun add(expense: Expense) {
        expenseDao.add(expense)
    }

    suspend fun update(expense: Expense) {
        expenseDao.update(expense)
    }

    fun delete(expense: Expense) {
        expenseDao.delete(expense)
    }

    val readAllData: LiveData<List<Expense>> = expenseDao.readData()

    fun readSelectedData(context: Context): List<Expense> {
        return readAllData.value?.filter { expense -> expense.isSelected(context) } ?: emptyList()
    }

    val readSavedData: LiveData<List<SavedDate>> = expenseDao.readSavedData()

    fun refreshData(dataChanged: Long) = expenseDao.refreshData(dataChanged)
}