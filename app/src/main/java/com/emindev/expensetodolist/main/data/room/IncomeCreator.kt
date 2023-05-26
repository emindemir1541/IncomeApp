package com.emindev.expensetodolist.main.data.room


import android.content.Context
import androidx.lifecycle.LiveData
import com.emindev.expensetodolist.expense.common.util.Message
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class IncomeCreator(private val dao:IncomeDao) {


    suspend fun upsert(income: Income) {
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
                dao.upsert(newIncome)

            }
        }
        else {
            dao.upsert(income)
        }
    }

    suspend fun updateOne(income: Income) {
        dao.upsert(income)
    }

    suspend fun updateAll(income: Income, incomeList: List<Income>) {
        if (income.isRepeatable) {
            incomeList.forEach { exIncome ->
                if (income.date >= DateUtil.currentDateTime.toLocalDate()) {
                    val newIncome = exIncome.copy(
                        name = income.name,
                        amount = income.amount
                    )
                    dao.upsert(newIncome)
                }
                else {
                    val newIncome = exIncome.copy(
                        name = income.name
                    )
                    dao.upsert(newIncome)
                }
            }
        }
        else {
            dao.upsert(income)

        }
    }

    suspend fun delete(income: Income, incomeList: List<Income>) {
        if (income.deleted || !income.isRepeatable) {
            dao.delete(income.id)
            withContext(Dispatchers.Main) {
               // message.infoDeletedCard()
            }
        }
        else {
            incomeList.forEach { exIncome ->
                exIncome.deleted = true
                dao.upsert(exIncome)
                if (exIncome.date >= DateUtil.currentDateTime.toLocalDate()) {
                    dao.delete(exIncome.id)
                }
            }
            withContext(Dispatchers.Main) {
                //message.infoDeletedAfterNow()
            }
        }
    }

    fun readSelectedData(month:Int,year:Int): Flow<List<Income>> = dao.readSelectedData(month,year)

/*    fun readSelectedData(context: Context): List<Income> {
        return readAllData.value?.filter { income -> income.isSelected(context) } ?: emptyList()
    }*/


}
