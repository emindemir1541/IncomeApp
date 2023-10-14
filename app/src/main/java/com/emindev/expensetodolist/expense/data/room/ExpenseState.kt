package com.emindev.expensetodolist.expense.data.room

import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.main.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import com.emindev.expensetodolist.main.common.util.toFloatOrZero
import java.time.LocalDate

data class ExpenseState(
    val expensesOneCard:List<Expense> = emptyList(),
    val expensesMultipleCardCompleted:List<Expense> = emptyList(),
    val expensesMultipleCardNotCompleted:List<Expense> = emptyList(),
    val expenseInfinityModels:List<ExpenseModel> = emptyList(),
    val id: Long = 0,
    val cardId: Long= 0,
    val name: String = "",
    val latestAmount: String= "",
    val currentAmount: String = "",
    val initialDate: LocalDate = DateUtil.localDateNow,
    val currentDate: LocalDate = DateUtil.localDateNow,
    val completed: Boolean = false,
    val deleted: Boolean = false,
    val cardDeleted: Boolean = false,
    val repeatType: RepeatType = RepeatType.INFINITY,
    val repetition: String = "",
    val expenseType: ExpenseType = ExpenseType.NEED,
    val lender: String = "",
    val isAddingExpense:Boolean = false
){
    fun toExpense() = Expense(id=id,cardId = cardId,name = name, latestAmount = latestAmount.toFloatOrZero, currentDate = SqlDateUtil.convertDate(currentDate), currentAmount = currentAmount.toFloatOrZero, initialDate = SqlDateUtil.convertDate(initialDate), completed = completed, deleted = deleted, cardDeleted = cardDeleted, repeatType = repeatType, expenseType = expenseType, repetition = repetition.toIntOrNull(), lender = lender)
}