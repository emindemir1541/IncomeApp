package com.emindev.expensetodolist.expense.data.room

import com.emindev.expensetodolist.expense.common.constant.ExpenseType

sealed interface ExpenseEvent {
    object SaveExpense: ExpenseEvent
    object UpdateExpense: ExpenseEvent
    data class SetName(val name:String): ExpenseEvent
    data class SetAmount(val amount:Float): ExpenseEvent
    data class SetStartedDate(val startedDate:Long): ExpenseEvent
    data class SetCompleted(val completed:Boolean): ExpenseEvent
    data class SetLenderName(val lender:String?): ExpenseEvent
    data class SetRepetition(val repetition:Int?): ExpenseEvent
    data class SetDeleted(val deleted:Boolean): ExpenseEvent
    data class SetExpenseType(val type:ExpenseType): ExpenseEvent
    data class SetDay(val day:Int): ExpenseEvent
    data class SetMonth(val month: Int): ExpenseEvent
    data class SetYear(val year: Int): ExpenseEvent
    data class SetCardId(val cardId: Long): ExpenseEvent
    object ShowDialog: ExpenseEvent
    object HideDialog: ExpenseEvent
    data class DeleteExpense(val expense: Expense): ExpenseEvent

}