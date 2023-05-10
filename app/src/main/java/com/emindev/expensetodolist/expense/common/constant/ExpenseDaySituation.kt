package com.emindev.expensetodolist.expense.common.constant

import android.content.Context
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.expense.common.util.remainingDay
import com.emindev.expensetodolist.expense.data.room.Expense
import kotlin.math.abs

sealed class ExpenseDaySituation(val situation: String,val day:Long? = null) {
    class DayRemained(val expense: Expense,val context: Context):ExpenseDaySituation(expense.remainingDay.toString() + " " + context.getString(R.string.day_remained),expense.remainingDay)
    class Today(val context: Context):ExpenseDaySituation(context.getString(R.string.payment_time))
    class DayPassed(val expense: Expense,val context: Context):ExpenseDaySituation(abs(expense.remainingDay).toString() +" "+ context.getString(R.string.day_passed),abs(expense.remainingDay))
    class Paid(val context: Context):ExpenseDaySituation(context.getString(R.string.expense_paid))
}