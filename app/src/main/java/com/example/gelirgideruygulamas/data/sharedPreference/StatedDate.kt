package com.example.gelirgideruygulamas.data.sharedPreference

import android.content.Context
import com.example.gelirgideruygulamas.helper.DateHelper
import com.example.gelirgideruygulamas.helper.DateHelper.Companion.checkMonthAndYear
import java.time.DateTimeException
import java.time.LocalDateTime

private const val SAVED_DATE = "SAVED_DATE"
private const val DATE_IN_MILLIS = "DATE_IN_MILLIS"

class StatedDate(private val context: Context) {

    private val repository = SharedPreferenceRepository(context)

    fun setDate(value: Long) {
        repository.setLong(SAVED_DATE, DATE_IN_MILLIS, value)
    }

    fun setDate(dateTime: LocalDateTime) {
        repository.setLong(SAVED_DATE, DATE_IN_MILLIS, DateHelper.toLong(dateTime))
    }

    fun setToday(): String {
        setDate(DateHelper().currentTime)
        return getMonth()
    }

    fun getDateLong(): Long {
        return repository.getLong(SAVED_DATE, DATE_IN_MILLIS)
    }

    fun getDateString(): String {
        return DateHelper.convertToString(getDateLong())
    }

    fun getDateTime(): LocalDateTime {
        return DateHelper.convertToDateTime(repository.getLong(SAVED_DATE, DATE_IN_MILLIS))
    }

    fun getMonth(): String {
        return DateHelper.getMonth(repository.getLong(SAVED_DATE, DATE_IN_MILLIS))
    }

    fun addMonth() {
        setDate(DateHelper.plusMonth(getDateTime(), 1))
    }

    fun subtractMonth() {
        setDate(DateHelper.minusMonth(getDateTime(), 1))
    }

    fun isToday():Boolean{
        return getDateTime().checkMonthAndYear(DateHelper().currentDateTime)
    }

}