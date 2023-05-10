package com.emindev.expensetodolist.main.data.sharedPreference

import android.content.Context
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.toLong
import com.emindev.expensetodolist.helperlibrary.data.sharedpreference.SharedPreferenceRepository
import com.emindev.expensetodolist.main.common.constant.SharedPrefFileNames
import com.emindev.expensetodolist.main.common.constant.SharedPrefStatedDate
import java.time.LocalDateTime

class StatedDate(private val context: Context) {

    private val repository = SharedPreferenceRepository(context)

    fun setDate(value: Long) {
        repository.setLong(SharedPrefFileNames.STATED_DATE, SharedPrefStatedDate.DATE_IN_MILLIS, value)
    }

    fun setDate(dateTime: LocalDateTime) {
        repository.setLong(SharedPrefFileNames.STATED_DATE, SharedPrefStatedDate.DATE_IN_MILLIS, dateTime.toLong())
    }

    fun setToday(): String {
        setDate(DateUtil.currentTime)
        return month
    }

    val dateLong: Long
        get() {
            return repository.getLong(SharedPrefFileNames.STATED_DATE, SharedPrefStatedDate.DATE_IN_MILLIS,DateUtil.currentTime)
        }

    val dateString: String
        get() {
            return DateUtil.convertToString(dateLong)
        }

    val dateTime: LocalDateTime
        get() {
            return DateUtil.convertToDateTime(repository.getLong(SharedPrefFileNames.STATED_DATE, SharedPrefStatedDate.DATE_IN_MILLIS,DateUtil.currentTime))
        }

    val month: String
        get() {
            return DateUtil.getMonth(repository.getLong(SharedPrefFileNames.STATED_DATE, SharedPrefStatedDate.DATE_IN_MILLIS,DateUtil.currentTime))
        }

    fun addMonth() {
        setDate(DateUtil.plusMonth(dateTime, 1))
    }

    fun subtractMonth() {
        setDate(DateUtil.minusMonth(dateTime, 1))
    }

    val isToday: Boolean
        get() {
            return dateTime.checkMonthAndYear(DateUtil.currentDateTime)
        }

}