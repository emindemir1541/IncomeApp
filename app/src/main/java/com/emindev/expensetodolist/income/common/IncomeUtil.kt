package com.emindev.expensetodolist.income.common

import android.content.Context
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.emindev.expensetodolist.main.data.room.Income
import java.time.LocalDate


fun Income.remainingDay(mContext:Context): String = if (isMoneyPaid) mContext.getString(R.string.paid) else (DateUtil.dayBetweenTwoDate(currentDate, DateUtil.currentDateTime.toLocalDate())).toString() +" "+ mContext.getString(R.string.day_remained)

val Income.initialDate: LocalDate
    get() = DateUtil.convertToDate(_initialDate)

val Income.currentDate: LocalDate
    get() = DateUtil.convertToDate(_currentDate)

val Income.isMoneyPaid:Boolean
    get() = currentDate.dayOfMonth <= DateUtil.currentDateTime.dayOfMonth && currentDate.checkMonthAndYear(DateUtil.currentDateTime)
