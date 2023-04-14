package com.example.gelirgideruygulamas.main.data.expense

import android.content.Context
import androidx.lifecycle.LiveData

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    suspend fun add(expense: Expense) {
            expenseDao.add(expense)
    }

    suspend fun update(expense: Expense) {
        expenseDao.update(expense)
    }

    suspend fun delete(expense: Expense) {
        expenseDao.delete(expense)
    }

    suspend fun deleteByCardId(expense: Expense){
        expenseDao.deleteByCardId(expense.cardId)
    }

     val readAllData: LiveData<List<Expense>> = expenseDao.readData()

    fun readSelectedData(context: Context): List<Expense> {
        return readAllData.value?.filter { expense -> expense.isSelected(context) } ?: emptyList()
    }

    fun readDataByCardId(cardId:Long):LiveData<List<Expense>> = expenseDao.readDataByCardId(cardId)

   suspend fun refreshData(dataChanged: Long) = expenseDao.refreshData(dataChanged)

}

// val readSavedData: LiveData<List<SavedDate>> = expenseDao.readSavedData()
