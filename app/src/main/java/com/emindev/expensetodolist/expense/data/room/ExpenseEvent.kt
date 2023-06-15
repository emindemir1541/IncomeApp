package com.emindev.expensetodolist.expense.data.room

import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.main.common.constant.RepeatType
import java.time.LocalDate

sealed interface ExpenseEvent {
    object SaveExpense: ExpenseEvent
    object UpdateExpense: ExpenseEvent
    data class SetName(val name:String): ExpenseEvent
    data class SetAmount(val amount:String): ExpenseEvent
    data class SetDate(val date:LocalDate): ExpenseEvent
    data class SetCompleted(val completed:Boolean): ExpenseEvent
    data class SetLenderName(val lender:String): ExpenseEvent
    data class SetRepeatType(val repeatType:RepeatType): ExpenseEvent
    data class SetRepetition(val repetition:String?): ExpenseEvent
    data class SetExpenseType(val type:ExpenseType): ExpenseEvent
    data class Complete(val expense: Expense):ExpenseEvent
    object ShowDialog: ExpenseEvent
    object HideDialog: ExpenseEvent
    data class DeleteExpense(val expense: Expense): ExpenseEvent
    data class DeleteCard(val expense: Expense): ExpenseEvent

}