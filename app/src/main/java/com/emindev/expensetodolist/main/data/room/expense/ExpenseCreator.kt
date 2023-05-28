package com.emindev.expensetodolist.main.data.room.expense

import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.expense.common.constant.ExpenseSituation
import com.emindev.expensetodolist.expense.common.util.getCardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenseCreator(private val dao: ExpenseDao) {


    suspend fun add(expense: Expense) {

        val cardId = DateUtil.dateTimeNow
        if (expense.repetition != null) {

            repeat(expense.repetition!!) {
                val date = expense.startedDate.plusMonths(it.toLong())

                val newExpense = expense.copy(day = date.dayOfMonth, month = date.monthValue, year = date.year, cardId = cardId)
                dao.upsert(newExpense)

            }
        }
        else {
            dao.upsert(expense)
        }
    }

    suspend fun updateOne(expense: Expense) {
        dao.upsert(expense)
    }

    suspend fun updateAll(expense: Expense, expenseList: List<Expense>) {
        if (expense.getCardType() == ExpenseSituation.ONCE) {
            dao.upsert(expense)
        }
        else {
            expenseList.forEach { exExpense ->
                if (expense.date >= DateUtil.localDateTimeNow.toLocalDate()) {

                    val newExpense = exExpense.copy(name = expense.name, amount = expense.amount, deleted = expense.deleted)

                    dao.upsert(newExpense)
                }
                else {

                    val newExpense = exExpense.copy(name = expense.name, amount = expense.amount, deleted = expense.deleted)

                    dao.upsert(newExpense)
                }
            }
        }
    }

    suspend fun delete(expense: Expense, expenseList: List<Expense> = emptyList()) {
        if (expense.deleted || expense.getCardType() == ExpenseSituation.ONCE) {
            dao.delete(expense)
            withContext(Dispatchers.Main) {
                //message.infoDeletedCard()
            }
        }
        else {
            expenseList.forEach { exExpense: Expense ->
                exExpense.deleted = true
                dao.upsert(expense)
                if (exExpense.date >= DateUtil.localDateTimeNow.toLocalDate()) {
                    dao.delete(exExpense)
                }
            }
            withContext(Dispatchers.Main) {
                // message.infoDeletedAfterNow()
            }
        }
    }

    fun readSelectedData(month:Int,year: Int) = dao.readSelectedData(month,year)

}

/* fun readSelectedData(context: Context): List<Expense> {
         return readAllData.value?.filter { expense -> expense.isSelected(context) } ?: emptyList()
     }*/