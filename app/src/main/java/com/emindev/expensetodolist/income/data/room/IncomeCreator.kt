package com.emindev.expensetodolist.income.data.room


import android.content.Context
import androidx.lifecycle.LiveData
import com.emindev.expensetodolist.expense.common.util.Message
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IncomeCreator(context: Context) {

    private val repository = IncomeRepository(context)
    private val message = Message(context)

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
            withContext(Dispatchers.Main) {
                message.infoDeletedCard()
            }
        }
        else {
            incomeList.forEach { exIncome ->
                exIncome.deleted = true
                repository.update(exIncome)
                if (exIncome.date >= DateUtil.currentDateTime.toLocalDate()) {
                    repository.delete(exIncome)
                }
            }
            withContext(Dispatchers.Main) {
                message.infoDeletedAfterNow()
            }
        }
    }

    val readAllData: LiveData<List<Income>> = repository.readAllData

    val readSelectedData: LiveData<List<Income>> get() = repository.readSelectedData

/*    fun readSelectedData(context: Context): List<Income> {
        return readAllData.value?.filter { income -> income.isSelected(context) } ?: emptyList()
    }*/


}
