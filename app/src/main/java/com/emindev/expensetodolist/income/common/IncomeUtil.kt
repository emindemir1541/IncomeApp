package com.emindev.expensetodolist.income.common

import android.content.Context
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.emindev.expensetodolist.main.data.room.Income
import com.emindev.expensetodolist.main.data.sharedPreference.StatedDate

fun Income.isSelected(context: Context): Boolean {
    //cart StatedDate de kaydedilen tarihle uyuşuyor mu
    return StatedDate(context).dateTime.checkMonthAndYear(date)
}

fun Income.remainingDay(mContext:Context): String = if (itsTime) mContext.getString(R.string.paid) else (DateUtil.dayBetweenTwoDate(date, DateUtil.currentDateTime.toLocalDate())).toString() +" "+ mContext.getString(R.string.day_remained)
