package com.example.gelirgideruygulamas.main.data.expense

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.main.common.util.isSelected
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate

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

    suspend fun deleteByCardId(expense: Expense) {
        expenseDao.deleteByCardId(expense.cardId)
    }

    val readAllData: LiveData<List<Expense>> = expenseDao.readData()

    fun getReadSelectedData(context: Context): LiveData<List<Expense>> {
        val currentDate = StatedDate(context).dateTime
        return expenseDao.readSelectedData(currentDate.monthValue, currentDate.year)
    }

    /*fun readSelectedData(context: Context): List<Expense> {
        return readAllData.value?.filter { expense -> expense.isSelected(context) } ?: emptyList()
    }*/

    fun readDataByCardId(cardId: Long): LiveData<List<Expense>> = expenseDao.readDataByCardId(cardId)

    suspend fun refreshData(dataChanged: Long) = expenseDao.refreshData(dataChanged)

}

// val readSavedData: LiveData<List<SavedDate>> = expenseDao.readSavedData()
