package com.emindev.expensetodolist.expense.data.room

import androidx.room.Entity
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import java.time.LocalDate

@Entity
data class Expense(
    val id: Long,
    val cardId: Long,
    val name: String,
    val latestAmount: Float,
    val currentAmount: Float,
    val initialDate: String,
    val currentDate: String,
    val completed: Boolean?,
    val deleted: Boolean,
    val cardDeleted: Boolean,
    val repeatType: RepeatType,
    val repetition: Int?,
    val expenseType: ExpenseType,
    val lender: String,
) {
    val initialLocalDate: LocalDate
        get() = SqlDateUtil.convertDate(initialDate)

    val currentLocalDate: LocalDate
        get() = SqlDateUtil.convertDate(currentDate)

    val toExpenseModel: ExpenseModel
        get() = ExpenseModel(name = name, latestAmount = latestAmount, initialDate = initialDate, deleted = deleted, repeatType = repeatType, repetition = repetition, expenseType = expenseType, lender = lender, id = id)

    val toExpenseCardModel: ExpenseCardModel
        get() = ExpenseCardModel(id = id, currentDate = currentDate, currentAmount = currentAmount, completed = completed, cardDeleted = cardDeleted, cardId = cardId)


}
