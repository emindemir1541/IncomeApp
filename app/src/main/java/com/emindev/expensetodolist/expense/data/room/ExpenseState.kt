package com.emindev.expensetodolist.expense.data.room

import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.constant.RepeatType
import java.time.LocalDate

data class ExpenseState(
    val expensesOneCard:List<Expense> = emptyList(),
    val expensesMultipleCard:List<Expense> = emptyList(),
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
)