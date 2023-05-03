package com.example.gelirgideruygulamas.expense.common.constant

import android.content.Context
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.expense.common.util.remainingDay
import com.example.gelirgideruygulamas.expense.data.room.Expense
import kotlin.math.abs

sealed class ExpenseDaySituation(val situation: String,val day:Long? = null) {
    class DayRemained(val expense: Expense,val context: Context):ExpenseDaySituation(expense.remainingDay.toString() + " " + context.getString(R.string.day_remained),expense.remainingDay)
    class Today(val context: Context):ExpenseDaySituation(context.getString(R.string.payment_time))
    class DayPassed(val expense: Expense,val context: Context):ExpenseDaySituation(abs(expense.remainingDay).toString() +" "+ context.getString(R.string.day_passed),abs(expense.remainingDay))
    class Paid(val context: Context):ExpenseDaySituation(context.getString(R.string.expense_paid))
}