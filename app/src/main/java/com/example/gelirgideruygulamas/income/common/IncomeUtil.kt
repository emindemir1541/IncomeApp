package com.example.gelirgideruygulamas.income.common

import android.content.Context
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.example.gelirgideruygulamas.income.data.room.Income
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate

fun Income.isSelected(context: Context): Boolean {
    //cart StatedDate de kaydedilen tarihle uyuşuyor mu
    return StatedDate(context).dateTime.checkMonthAndYear(date)
}

fun Income.remainingDay(mContext:Context): String = if (itsTime) mContext.getString(R.string.paid) else (DateUtil.dayBetweenTwoDate(date, DateUtil.currentDateTime.toLocalDate())).toString() +" "+ mContext.getString(R.string.day_remained)
