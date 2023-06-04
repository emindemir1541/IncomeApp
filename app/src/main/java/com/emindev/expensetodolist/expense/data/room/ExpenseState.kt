package com.emindev.expensetodolist.expense.data.room

import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import java.time.LocalDate

data class ExpenseState (
    val expensesOneCard:List<Expense> = emptyList(),
    val expensesMultipleCard:List<Expense> = emptyList(),
    val selectedDate: LocalDate = DateUtil.localDateTimeNow.toLocalDate(),
    val name:String = "",
    val amount:String = "",
    val startedDate:Long =0,
    val completed:Boolean = false,
    val lender:String = "",
    val repetitionSituation:Boolean = false,
    val repetition:String = "",
    val deleted:Boolean = false,
    val type:ExpenseType = ExpenseType.NEED,
    val day:Int = 0,
    val month:Int = 0,
    val year:Int = 0,
    val cardId:Long = 0,
    val isAddingExpense:Boolean = false
)