package com.example.gelirgideruygulamas.data.income

import android.content.Context
import androidx.lifecycle.LiveData

class IncomeRepository(private val incomeDao: IncomeDao) {

    suspend fun add(income: Income) {

        incomeDao.add(income)
    }

    suspend fun update(income: Income) {
        incomeDao.update(income)
    }

    suspend fun delete(id: Int) {
        incomeDao.delete(id)
    }

    suspend fun deleteAll() {
        incomeDao.deleteAll()
    }

    val readAllData: LiveData<List<Income>> = incomeDao.readAllData()

    fun readSelectedData(context: Context): List<Income> {
        return readAllData.value?.filter { income -> income.isSelected(context) } ?: emptyList()
    }

    fun refreshData(dataChanged: Long) = incomeDao.refreshData(dataChanged)

}

/*fun readDataByDate(payDay:String):LiveData<List<Income>> = incomeDao.readDataByDate(payDay)*/
