package com.example.gelirgideruygulamas.income.data.room


import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.toLong

class IncomeCreator(private val incomeDao: IncomeDao) {

    private val repository = IncomeRepository(incomeDao)


    suspend fun add(income: Income) {
        val cardId = DateUtil.currentTime
        if (income.repetation) {

            repeat(24) {
                var newIncome = Income(
                    income.name,
                    income.amount,
                    income.startedDateLong,
                    income.startedDate.plusMonths(it.toLong()).toLong(),
                    false,
                    income.repetation,
                    income.dataChanged,
                    cardId,
                )
                repository.add(newIncome)

            }
        }
        else {
            repository.add(income)
        }
    }

    suspend fun updateOne(income: Income) {
        repository.update(income)
    }

    suspend fun updateAll(income: Income, incomeList: List<Income>) {
        if (income.repetation) {
            incomeList.forEach { exIncome ->
                if (income.date >= DateUtil.currentDateTime.toLocalDate()) {
                    val newIncome = Income(
                        income.name,
                        income.amount,
                        exIncome.startedDateLong,
                        exIncome.dateLong,
                        exIncome.deleted,
                        exIncome.repetation,
                        exIncome.dataChanged,
                        exIncome.cardId,
                        exIncome.id
                    )
                    repository.update(newIncome)
                }
                else {
                    val newIncome = Income(
                        income.name,
                        exIncome.amount,
                        exIncome.startedDateLong,
                        exIncome.dateLong,
                        exIncome.deleted,
                        exIncome.repetation,
                        exIncome.dataChanged,
                        exIncome.cardId,
                        exIncome.id
                    )
                    repository.update(newIncome)
                }
            }
        }
        else {
            repository.update(income)

        }
    }

    suspend fun delete(income: Income, incomeList: List<Income>) {
        if (income.deleted || !income.repetation) {
            repository.delete(income)
            // TODO: 22.09.2022  message.infoDeletedCard()
        }
        else {
            incomeList.forEach { exIncome ->
                exIncome.deleted = true
                repository.update(exIncome)
                if (exIncome.date >= DateUtil.currentDateTime.toLocalDate()) {
                    repository.delete(exIncome)
                }
            }
            // TODO: 22.09.2022  message.infoDeletedAfterNow()
        }
    }

    val readAllData: LiveData<List<Income>> = incomeDao.readAllData()

    fun readSelectedData(context: Context): List<Income> {
        return readAllData.value?.filter { income -> income.isSelected(context) } ?: emptyList()
    }

    fun refreshData(dataChanged: Long) = incomeDao.refreshData(dataChanged)

}

/*fun readDataByDate(dateLong:String):LiveData<List<Income>> = incomeDao.readDataByDate(dateLong)*/
