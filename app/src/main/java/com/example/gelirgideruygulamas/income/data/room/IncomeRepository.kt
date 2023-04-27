package com.example.gelirgideruygulamas.income.data.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gelirgideruygulamas.helperlibrary.common.helper.test
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate

class IncomeRepository(private val context: Context) {

    private val incomeDao = IncomeDatabase.getDatabase(context).incomeDao()
    suspend fun add(income: Income) {
        incomeDao.add(income)
    }

    suspend fun update(income: Income) {
        incomeDao.update(income)
    }

    suspend fun delete(income: Income) {
        incomeDao.delete(income.id)
    }

    val readAllData: LiveData<List<Income>> = incomeDao.readAllData()

    val readSelectedData:LiveData<List<Income>>
        get() {
            val currentDate =StatedDate(context).dateTime
            return incomeDao.readSelectedData(currentDate.monthValue,currentDate.year)
        }

}

/*fun readDataByDate(dateLong:String):LiveData<List<Income>> = incomeDao.readDataByDate(dateLong)*/
