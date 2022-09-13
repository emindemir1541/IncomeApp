package com.example.gelirgideruygulamas.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

private const val PATTERN_TR = "dd/MM/yyyy"
private const val PATTERN_EU = "yyyy-MM-dd"


@SuppressLint("SimpleDateFormat")
class DateHelper {

    companion object {

        val currentTime: Long = toLong(LocalDateTime.now())
        val currentDateTime: LocalDateTime = LocalDateTime.now()

        private fun formatter() = SimpleDateFormat(PATTERN_TR)
        private fun splitDate(date: String): MutableList<String> = date.split("/").toMutableList()


        //Conversations
        fun convertToString(day: Int, month: Int, year: Int): String = "$day/$month/$year "
        fun convertToString(timeInMillis: Long): String = android.text.format.DateFormat.format(PATTERN_TR, timeInMillis).toString()
        fun convertToString(dateTime: LocalDateTime): String = "${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year}"
        private fun convertToStringEu(timeInMillis: Long): String = android.text.format.DateFormat.format(PATTERN_EU, timeInMillis).toString()

        fun convertToDateTime(dateTime: Long): LocalDateTime {
            return Instant.ofEpochMilli(dateTime).atZone(ZoneId.systemDefault()).toLocalDateTime()
        }

        fun convertToDateTime(year: Int, month: Int, dayOfMonth: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, nanoOfSecond: Int = 0): LocalDateTime {
            return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(ZoneId.systemDefault()).toLocalDateTime()
        }

        fun toLong(localDateTime: LocalDateTime): Long {
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        fun toLong(year: Int, month: Int, dayOfMonth: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, nanoOfSecond: Int = 0): Long {
            return convertToDateTime(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        //add and subtract
        fun plusMonth(dateTime: LocalDateTime, months: Long): Long = toLong(dateTime.plusMonths(months))
        fun plusMonth(dateTime: Long, months: Long): Long = toLong(convertToDateTime(dateTime).plusMonths(months))
        fun minusMonth(dateTime: LocalDateTime, months: Long): Long = toLong(dateTime.minusMonths(months))
        fun minusMonth(dateTime: Long, months: Long): Long = toLong(convertToDateTime(dateTime).plusMonths(months))

        fun getMonth(localDateTime: LocalDateTime): String = localDateTime.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        fun getMonth(localDateTime: Long): String = convertToDateTime(localDateTime).month.getDisplayName(TextStyle.FULL, Locale.getDefault())

        fun LocalDateTime.ifToday(): Boolean {
            val date = convertToDateTime(currentTime)
            return this.dayOfMonth == date.dayOfMonth && this.month == date.month && this.year == date.year
        }

        fun Long.ifToday(): Boolean {
            val currentDate = convertToDateTime(currentTime)
            val selectedDate = convertToDateTime(this)
            return currentDate.toLocalDate() == selectedDate.toLocalDate()
        }

        fun LocalDateTime.checkMonthAndYear(localDateTime: LocalDateTime): Boolean {
            return localDateTime.monthValue == this.monthValue && localDateTime.year == this.year
        }
    }


    //fun getLocalDate(): String = DateTimeFormatter.ofPattern(UTC_TR).format(LocalDate.now())
    /*  fun dateToMonthAndYear(date: String): String {
        val splintedDate = date.split("/")
        return splintedDate[1] + "/" + splintedDate[2]
    }*/
    //fun convertToString(day: Int, month: Int, year: Int): String = "$day/$month/$year"
    //fun convertToTimeInMillis(date: String): Long = convertToDate(date).time
    //fun convertToDate(date: String): java.util.Date = formatter().parse(date)
    //fun convertToDate(date:Long):java.util.Date= convertToDate(convertToString(date))

}







