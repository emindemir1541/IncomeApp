package com.emindev.expensetodolist.expense.common.util

import android.content.Context
import com.emindev.expensetodolist.expense.common.constant.ExpenseDaySituation
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.emindev.expensetodolist.expense.common.constant.ExpenseSituation
import com.emindev.expensetodolist.main.data.room.Expense

/*fun Expense.remainingDay(mContext: Context): String {
    val remainingDay = (DateUtil.dayBetweenTwoDate(date, DateUtil.currentDateTime.toLocalDate()))
    return if (completed) {
        mContext.getString(R.string.expense_paid)
    }
    else if (isMoneyPaid) {
        mContext.getString(R.string.payment_time)
    }
    else {
        if (remainingDay > 0)
            remainingDay.toString() + " " + mContext.getString(R.string.day_remained)
        else
            abs(remainingDay).toString() + " " + mContext.getString(R.string.day_passed)
    }
}*/

val Expense.remainingDay: Long
    get() = DateUtil.dayBetweenTwoDate(date, DateUtil.currentDateTime.toLocalDate())

fun Expense.daySituation(context: Context): ExpenseDaySituation {
    return if (completed) {
        ExpenseDaySituation.Paid(context)
    }
    else if (itsTime) {
        ExpenseDaySituation.Today(context)
    }
    else if (remainingDay >= 0)
        ExpenseDaySituation.DayRemained(this, context)
    else
        ExpenseDaySituation.DayPassed(this, context)
}


fun Expense.getCardType(): ExpenseSituation {
    return when {
        repetition == null -> {
            ExpenseSituation.ONCE
        }
        completed -> {
            ExpenseSituation.DONE
        }
        else -> ExpenseSituation.UNDONE
    }
}