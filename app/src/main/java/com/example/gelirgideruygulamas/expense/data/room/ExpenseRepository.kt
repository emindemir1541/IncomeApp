package com.example.gelirgideruygulamas.expense.data.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate

class ExpenseRepository(private val context: Context) {

    private val expenseDao:ExpenseDao = ExpenseDatabase.getDatabase(context).expenseDao()

    suspend fun add(expense: Expense) {
        expenseDao.add(expense)
    }

    suspend fun update(expense: Expense) {
        expenseDao.update(expense)
    }

    suspend fun delete(expense: Expense) {
        expenseDao.delete(expense)
    }

  /*  suspend fun deleteByCardId(expense: Expense) {
        expenseDao.deleteByCardId(expense.cardId)
    }*/

    val readAllData: LiveData<List<Expense>> = expenseDao.readData()

    val readSelectedData: LiveData<List<Expense>>
        get() {
            val currentDate = StatedDate(context).dateTime
            return expenseDao.readSelectedData(currentDate.monthValue, currentDate.year)
        }



    /*fun readDataByCardId(cardId: Long): LiveData<List<Expense>> = expenseDao.readDataByCardId(cardId)*/


}

/*fun readSelectedData(context: Context): List<Expense> {
    return readAllData.value?.filter { expense -> expense.isSelected(context) } ?: emptyList()
}*/

// val readSavedData: LiveData<List<SavedDate>> = expenseDao.readSavedData()
