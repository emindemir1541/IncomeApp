package com.emindev.expensetodolist.expense.data.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.expense.common.constant.ExpenseSituation
import com.emindev.expensetodolist.expense.common.util.Message
import com.emindev.expensetodolist.expense.common.util.getCardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenseCreator(context: Context) {

    private val repository = ExpenseRepository(context)
    private val message = Message(context)

    suspend fun add(expense: Expense) {

        val cardId = DateUtil.currentTime
        if (expense.repetition != null) {

            repeat(expense.repetition!!) {
                val date = expense.startedDate.plusMonths(it.toLong())

                val newExpense = expense.copy(day = date.dayOfMonth, month = date.monthValue, year = date.year, cardId = cardId)
                repository.add(newExpense)

            }
        }
        else {
            repository.add(expense)
        }
    }

    suspend fun updateOne(expense: Expense) {
        repository.update(expense)
    }

    suspend fun updateAll(expense: Expense, expenseList: List<Expense>) {
        if (expense.getCardType() == ExpenseSituation.ONCE) {
            repository.update(expense)
        }
        else {
            expenseList.forEach { exExpense ->
                if (expense.date >= DateUtil.currentDateTime.toLocalDate()) {

                    val newExpense = exExpense.copy(name = expense.name, amount = expense.amount, deleted = expense.deleted)

                    repository.update(newExpense)
                }
                else {

                    val newExpense = exExpense.copy(name = expense.name, amount = expense.amount, deleted = expense.deleted)

                    repository.update(newExpense)
                }
            }
        }
    }

    suspend fun delete(expense: Expense, expenseList: List<Expense> = emptyList()) {
        if (expense.deleted || expense.getCardType() == ExpenseSituation.ONCE) {
            repository.delete(expense)
            withContext(Dispatchers.Main){
                message.infoDeletedCard()
            }
        }
        else {
            expenseList.forEach { exExpense: Expense ->
                exExpense.deleted = true
                repository.update(exExpense)
                if (exExpense.date >= DateUtil.currentDateTime.toLocalDate()) {
                    repository.delete(exExpense)
                }
            }
            withContext(Dispatchers.Main){
                message.infoDeletedAfterNow()
            }
        }
    }

    val readAllData: LiveData<List<Expense>> = repository.readAllData

    val readSelectedData: LiveData<List<Expense>>
        get() = repository.readSelectedData

    /*fun readDataByCardId(cardId: Long): LiveData<List<Expense>> = repository.readDataByCardId(cardId)*/

}

/* fun readSelectedData(context: Context): List<Expense> {
         return readAllData.value?.filter { expense -> expense.isSelected(context) } ?: emptyList()
     }*/