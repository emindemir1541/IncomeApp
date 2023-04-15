package com.example.gelirgideruygulamas.main.common.util

import android.content.Context
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.example.gelirgideruygulamas.main.common.constant.ExpenseCardSituation
import com.example.gelirgideruygulamas.main.data.expense.Expense
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate

fun Expense.remainingDay(mContext: Context): String {
    return if (completed) {
        mContext.getString(R.string.expense_paid)
    }
    else if (itsTime) {
        mContext.getString(R.string.payment_time)
    }
    else {
        (DateUtil.dayBetweenTwoDate(date, DateUtil.currentDateTime.toLocalDate())).toString() + " " + mContext.getString(R.string.day_remained)
    }
}

fun Expense.isSelected(context: Context): Boolean {
    //cart, StatedDate de kaydedilen tarihle uyuşuyor mu
    return StatedDate(context).getDateTime().checkMonthAndYear(date)
}

fun Expense.getCardType(): ExpenseCardSituation {
    return when {
        repetition == null -> {
            ExpenseCardSituation.ONCE
        }
        completed -> {
            ExpenseCardSituation.DONE
        }
        else -> ExpenseCardSituation.UNDONE
    }
}