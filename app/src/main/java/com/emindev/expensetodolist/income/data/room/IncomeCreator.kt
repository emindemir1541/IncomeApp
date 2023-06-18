package com.emindev.expensetodolist.income.data.room
/*


import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class IncomeCreator(private val dao:IncomeDao) {


    suspend fun upsert(income: Income) {
        val cardId = DateUtil.dateTimeNow
        if (income.isRepeatable) {

            repeat(24) {
                val date = income.initialDate.plusMonths(it.toLong())

                val newIncome = income.copy(
                    day = date.dayOfMonth,
                    month = date.monthValue,
                    year = date.year,
                    cardDeleted = false,
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
                if (income.date >= DateUtil.localDateTimeNow.toLocalDate()) {
                    val newIncome = exIncome.copy(
                        route = income.route,
                        latestAmount = income.latestAmount
                    )
                    dao.upsert(newIncome)
                }
                else {
                    val newIncome = exIncome.copy(
                        route = income.route
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
        if (income.cardDeleted || !income.isRepeatable) {
            dao.delete(income)
            withContext(Dispatchers.Main) {
               // message.infoDeletedCard()
            }
        }
        else {
            incomeList.forEach { exIncome ->
                exIncome.cardDeleted = true
                dao.upsert(exIncome)
                if (exIncome.date >= DateUtil.localDateTimeNow.toLocalDate()) {
                    dao.delete(exIncome)
                }
            }
            withContext(Dispatchers.Main) {
                //message.infoDeletedAfterNow()
            }
        }
    }

    fun readSelectedData(month:Int,year:Int): Flow<List<Income>> = dao.readSelectedData(month,year)

*/
/*    fun readSelectedData(context: Context): List<Income> {
        return readAllData.value?.filter { income -> income.isSelected(context) } ?: emptyList()
    }*//*



}
*/