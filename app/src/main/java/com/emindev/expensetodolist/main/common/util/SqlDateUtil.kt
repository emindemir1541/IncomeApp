package com.emindev.expensetodolist.main.common.util

import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.convertToString
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.convertToStringReverted
import java.time.LocalDate

object SqlDateUtil {
    const val dateDelimiter = DateUtil.Delimiters.dash
    const val dateType = DateUtil.DateTypes.yyyy_MM_dd_dash

    fun convertDate(date: LocalDate): String {
        return date.convertToStringReverted(dateDelimiter)
    }
    fun convertDate(date:String): LocalDate {
        return DateUtil.convertToDate(date, dateType)
    }
}