package com.example.gelirgideruygulamas.data.sharedPreference

import android.content.Context
import com.example.gelirgideruygulamas.common.helper.DateUtil
import com.example.gelirgideruygulamas.common.helper.DateUtil.Companion.checkMonthAndYear
import com.example.gelirgideruygulamas.common.helper.DateUtil.Companion.toLong
import java.time.LocalDateTime

private const val SAVED_DATE = "SAVED_DATE"
private const val DATE_IN_MILLIS = "DATE_IN_MILLIS"

class StatedDate(private val context: Context) {

    private val repository = SharedPreferenceRepository(context)

    fun setDate(value: Long) {
        repository.setLong(SAVED_DATE, DATE_IN_MILLIS, value)
    }

    fun setDate(dateTime: LocalDateTime) {
        repository.setLong(SAVED_DATE, DATE_IN_MILLIS, dateTime.toLong())
    }

    fun setToday(): String {
        setDate(DateUtil().currentTime)
        return getMonth()
    }

    fun getDateLong(): Long {
        return repository.getLong(SAVED_DATE, DATE_IN_MILLIS)
    }

    fun getDateString(): String {
        return DateUtil.convertToString(getDateLong())
    }

    fun getDateTime(): LocalDateTime {
        return DateUtil.convertToDateTime(repository.getLong(SAVED_DATE, DATE_IN_MILLIS))
    }

    fun getMonth(): String {
        return DateUtil.getMonth(repository.getLong(SAVED_DATE, DATE_IN_MILLIS))
    }

    fun addMonth() {
        setDate(DateUtil.plusMonth(getDateTime(), 1))
    }

    fun subtractMonth() {
        setDate(DateUtil.minusMonth(getDateTime(), 1))
    }

    fun isToday():Boolean{
        return getDateTime().checkMonthAndYear(DateUtil().currentDateTime)
    }

    fun dateChanged(){

    }

}