package com.example.gelirgideruygulamas.main.data.expense

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.toLong
import com.example.gelirgideruygulamas.main.common.constant.ExpenseCardSituation
import com.example.gelirgideruygulamas.main.common.util.getCardType
import com.example.gelirgideruygulamas.main.common.util.isSelected

class ExpenseCreator(private val expenseDao: ExpenseDao) {

    private val repository = ExpenseRepository(expenseDao)

    suspend fun add(expense: Expense) {

        val cardId = DateUtil.currentTime
        if (expense.repetition != null) {

            repeat(expense.repetition!!) {
                val newExpense = Expense(
                    expense.name,
                    expense.amount,
                    expense.startedDateLong,
                    expense.completed,
                    expense.debt,
                    expense.lender,
                    expense.repetition,
                    expense.deleted,
                    expense.type,
                    expense.startedDate.plusMonths(it.toLong()).toLong(),
                    expense.dataChanged,
                    cardId,
                )
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
        // TODO: 11.10.2022 update ederken yeniden expense oluşturuyor,çünkü sıfırdan id üretiyor, gelir kısmı da böyle,düzelt 
        if (expense.getCardType() == ExpenseCardSituation.ONCE) {
            repository.update(expense)
        }
        else {
            expenseList.forEach { exExpense ->
                if (expense.date >= DateUtil.currentDateTime.toLocalDate()) {
                    val newExpense = Expense(
                        expense.name,
                        expense.amount,
                        exExpense.startedDateLong,
                        exExpense.completed,
                        exExpense.debt,
                        exExpense.lender,
                        exExpense.repetition,
                        expense.deleted,
                        exExpense.type,
                        exExpense.dateLong,
                        exExpense.dataChanged,
                        exExpense.cardId,
                        exExpense.id
                    )
                    repository.update(newExpense)
                }
                else {
                    val newExpense = Expense(
                        expense.name,
                        exExpense.amount,
                        exExpense.startedDateLong,
                        exExpense.completed,
                        exExpense.debt,
                        exExpense.lender,
                        exExpense.repetition,
                        expense.deleted,
                        exExpense.type,
                        exExpense.dateLong,
                        exExpense.dataChanged,
                        exExpense.cardId,
                        exExpense.id
                    )
                    repository.update(newExpense)
                }
            }
        }
    }

    suspend fun delete(expense: Expense, expenseList: List<Expense> = emptyList()) {
        if (expense.deleted || expense.getCardType() == ExpenseCardSituation.ONCE) {
            repository.delete(expense)
            // TODO: 22.09.2022  message.infoDeletedCard()
        }
        else {
            expenseList.forEach { exExpense: Expense ->
                exExpense.deleted = true
                repository.update(exExpense)
                if (exExpense.date >= DateUtil.currentDateTime.toLocalDate()) {
                    repository.delete(exExpense)
                }
            }
            // TODO: 22.09.2022  message.infoDeletedAfterNow()
        }
    }

    val readAllData: LiveData<List<Expense>> = repository.readAllData

    fun readSelectedData(context: Context): List<Expense> {
        return readAllData.value?.filter { expense -> expense.isSelected(context) } ?: emptyList()
    }

    fun readDataByCardId(cardId: Long): LiveData<List<Expense>> = expenseDao.readDataByCardId(cardId)

    suspend fun refreshData(dataChanged: Long) = expenseDao.refreshData(dataChanged)
}