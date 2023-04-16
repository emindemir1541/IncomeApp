package com.example.gelirgideruygulamas.main.data.sharedPreference

import android.content.Context
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.toLong
import com.example.gelirgideruygulamas.main.common.constant.SharedPrefFileNames
import com.example.gelirgideruygulamas.main.common.constant.SharedPrefStatedDate
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
            return repository.getLong(SharedPrefFileNames.STATED_DATE, SharedPrefStatedDate.DATE_IN_MILLIS)
        }

    val dateString: String
        get() {
            return DateUtil.convertToString(dateLong)
        }

    val dateTime: LocalDateTime
        get() {
            return DateUtil.convertToDateTime(repository.getLong(SharedPrefFileNames.STATED_DATE, SharedPrefStatedDate.DATE_IN_MILLIS))
        }

    val month: String
        get() {
            return DateUtil.getMonth(repository.getLong(SharedPrefFileNames.STATED_DATE, SharedPrefStatedDate.DATE_IN_MILLIS))
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