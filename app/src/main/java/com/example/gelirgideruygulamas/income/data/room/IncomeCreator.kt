package com.example.gelirgideruygulamas.income.data.room


import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil

class IncomeCreator(context: Context) {

    private val repository = IncomeRepository(context)


    suspend fun add(income: Income) {
        val cardId = DateUtil.currentTime
        if (income.isRepeatable) {

            repeat(24) {
                val date = income.startedDate.plusMonths(it.toLong())

                val newIncome = income.copy(
                    day = date.dayOfMonth,
                    month = date.monthValue,
                    year = date.year,
                    deleted = false,
                    cardId = cardId
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
        if (income.isRepeatable) {
            incomeList.forEach { exIncome ->
                if (income.date >= DateUtil.currentDateTime.toLocalDate()) {
                    val newIncome = exIncome.copy(
                        name = income.name,
                        amount = income.amount
                    )
                    repository.update(newIncome)
                }
                else {
                    val newIncome = exIncome.copy(
                        name = income.name
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
        if (income.deleted || !income.isRepeatable) {
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

    val readAllData: LiveData<List<Income>> = repository.readAllData

    val readSelectedData:LiveData<List<Income>> = repository.readSelectedData

/*    fun readSelectedData(context: Context): List<Income> {
        return readAllData.value?.filter { income -> income.isSelected(context) } ?: emptyList()
    }*/


}
