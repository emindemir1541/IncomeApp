package com.example.gelirgideruygulamas.data.expense

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gelirgideruygulamas.helper.DateHelper

class ExpenseCreator(private val expenseDao: ExpenseDao) {

    private val repository = ExpenseRepository(expenseDao)

    suspend fun add(expense: Expense) {

        val cardId = DateHelper().currentTime
        if (expense.repetition != null) {

            repeat(expense.repetition!!) {
                var newExpense = Expense(
                    expense.name,
                    expense.amount,
                    expense.startedDateLong,
                    expense.completed,
                    expense.debt,
                    expense.lender,
                    expense.repetition,
                    expense.deleted,
                    expense.type,
                    DateHelper.toLong(expense.startedDate.plusMonths(it.toLong())),
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

    suspend fun updateOne(expense: Expense, expenseList: List<Expense> = emptyList()) {
        repository.update(expense)
    }

    suspend fun updateAll(expense: Expense, expenseList: List<Expense>) {
        if (expense.getCardType() == ExpenseCardType.ONCE) {
            repository.update(expense)
        }
        else {
            expenseList.forEach { exExpense ->
                if (expense.date >= DateHelper().currentDateTime.toLocalDate()) {
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
        if (expense.deleted || expense.getCardType() == ExpenseCardType.ONCE) {
            repository.delete(expense)
            // TODO: 22.09.2022  message.infoDeletedCard()
        }
        else {
            expenseList.forEach { exExpense: Expense ->
                exExpense.deleted = true
                repository.update(exExpense)
                if (exExpense.dateLong >= DateHelper().currentTime) {
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