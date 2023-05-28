package com.emindev.expensetodolist.main.data.room.expense

import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import java.time.LocalDate
import java.time.LocalDateTime

data class ExpenseState (
    val expenses:List<Expense> = emptyList(),
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