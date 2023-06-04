package com.emindev.expensetodolist.helperlibrary.common.helper


import android.annotation.SuppressLint
import java.lang.IllegalArgumentException
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


@SuppressLint("SimpleDateFormat")
sealed interface DateUtil {

    object DateTypes {
        const val dd_MM_yyyy_slash = "dd/MM/yyyy"
        const val yyyy_MM_dd_slash = "yyyy/MM/dd"

        const val yyyy_MM_dd_dash = "yyyy-MM-dd"
        const val dd_MM_yyyy_dash = "dd-MM-yyyy"
    }

    object Delimiters {
        const val slash = "/"
        const val dash = "-"

    }

    companion object {


        val dateTimeNow: Long
            get() = LocalDateTime.now().toLong()
        val localDateTimeNow: LocalDateTime
            get() = LocalDateTime.now()

        val localDateNow: LocalDate
            get() = LocalDate.now()

        //Helpers
        private fun formatterTr(dateType: String) = DateTimeFormatter.ofPattern(dateType)

        fun Int.toDateString(): String {
            val value = this.toString()
            return if (value.length < 2)
                "0$value"
            else
                value
        }

        fun splitDate(date: String, delimiter: String): MutableList<String> = date.split(delimiter).toMutableList()

        //Conversations
        fun convertToString(day: Int, month: Int, year: Int, delimiter: String): String = "${day.toDateString()}$delimiter${month.toDateString()}$delimiter$year"
        fun convertToStringReverted(day: Int, month: Int, year: Int, delimiter: String): String = "$year$delimiter${month.toDateString()}$delimiter${day.toDateString()}"
        fun convertToString(timeInMillis: Long, dataType: String): String = android.text.format.DateFormat.format(dataType, timeInMillis).toString()

        // fun convertToString(dateTime: LocalDate,delimiter: String): String = "${dateTime.dayOfMonth.toDateString()}$delimiter${dateTime.monthValue.toDateString()}$delimiter${dateTime.year}"
        fun LocalDate.convertToString(delimiter: String): String = "${this.dayOfMonth.toDateString()}$delimiter${this.monthValue.toDateString()}$delimiter${this.year}"

        //  fun convertToStringReverted(dateTime: LocalDate,delimiter: String): String = "${dateTime.year}$delimiter${dateTime.monthValue.toDateString()}$delimiter${dateTime.dayOfMonth.toDateString()}"
        fun LocalDate.convertToStringReverted(delimiter: String): String = "${this.year}$delimiter${this.monthValue.toDateString()}$delimiter${this.dayOfMonth.toDateString()}"

        fun convertToDateTime(dateTime: Long): LocalDateTime = Instant.ofEpochMilli(dateTime).atZone(ZoneId.systemDefault()).toLocalDateTime()
        fun convertToDateTime(date: LocalDate, time: LocalTime = LocalTime.of(0, 0, 0)): LocalDateTime = LocalDateTime.of(date, time)
        fun convertToDate(date: String, dateFormat: String): LocalDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat))


        fun convertToDateTime(year: Int, month: Int, dayOfMonth: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, nanoOfSecond: Int = 0): LocalDateTime =
            LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(ZoneId.systemDefault()).toLocalDateTime()

        fun convertToDate(year: Int, month: Int, dayOfMonth: Int): LocalDate =
            LocalDate.of(year, month, dayOfMonth)

        fun LocalDateTime.toLong(): Long = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        fun LocalDate.toLong(): Long = convertToDateTime(this).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        fun toLong(year: Int, month: Int, dayOfMonth: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, nanoOfSecond: Int = 0): Long =
            convertToDateTime(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        //plus month
        //localDateTime.plusMonth
        //minus month
        //localDateTime.minusMonth

        fun LocalDateTime.monthString(): String = this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        fun LocalDate.monthString(): String = this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

        fun LocalDateTime.isToday(): Boolean {
            val date = convertToDateTime(dateTimeNow)
            return this.dayOfMonth == date.dayOfMonth && this.month == date.month && this.year == date.year
        }

        fun Long.ifToday(): Boolean {
            val currentDate = convertToDateTime(dateTimeNow)
            val selectedDate = convertToDateTime(this)
            return currentDate.toLocalDate() == selectedDate.toLocalDate()
        }

        //Month And Year
        fun LocalDateTime.isMonthAndYearEqualTo(localDateTime: LocalDateTime): Boolean = localDateTime.monthValue == this.monthValue && localDateTime.year == this.year
        fun LocalDate.isMonthAndYearEqualTo(localDate: LocalDate): Boolean = localDate.monthValue == this.monthValue && localDate.year == this.year
        fun LocalDate.isMonthAndYearBiggerThan(smallerLocalDate: LocalDate): Boolean = (smallerLocalDate.year < this.year) || (smallerLocalDate.year == this.year && smallerLocalDate.monthValue < this.monthValue)
        fun LocalDate.isMonthAndYearEqualOrBiggerThan(smallerLocalDate: LocalDate): Boolean = isMonthAndYearBiggerThan(smallerLocalDate) || isMonthAndYearEqualTo(smallerLocalDate)
        fun LocalDate.isMonthAndYearSmallerThan(biggerLocalDate: LocalDate): Boolean = (biggerLocalDate.year > this.year) || (biggerLocalDate.year == this.year && biggerLocalDate.monthValue > this.monthValue)
        fun LocalDate.isMonthAndYearEqualOrSmallerThan(biggerLocalDate: LocalDate): Boolean = isMonthAndYearSmallerThan(biggerLocalDate) || isMonthAndYearEqualTo(biggerLocalDate)
        fun LocalDate.toDateTime(): LocalDateTime = convertToDateTime(this)


        //difference between two getSavedDate
        fun dayBetweenTwoDate(dateTimeA: LocalDateTime, dateTimeB: LocalDateTime) = java.util.concurrent.TimeUnit.DAYS.convert(dateTimeA.toLong() - dateTimeB.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS)
        fun dayBetweenTwoDate(biggerDateTime: LocalDate, smallerDateTime: LocalDate) = java.util.concurrent.TimeUnit.DAYS.convert(biggerDateTime.toLong() - smallerDateTime.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS)
        fun dayBetweenTwoDate(dateTimeA: Long, dateTimeB: Long) = java.util.concurrent.TimeUnit.DAYS.convert(dateTimeA - dateTimeB, java.util.concurrent.TimeUnit.MILLISECONDS)

        fun forEachMonthBetweenTwoDate(includedFirstDate: LocalDate, includedLastDate: LocalDate, onEveryMonth: (LocalDate) -> Unit) {
            var currentDate = includedFirstDate

            if (includedLastDate < includedFirstDate)
                throw IllegalArgumentException("Last date must be bigger than first date")
            do {
                onEveryMonth.invoke(currentDate)
                currentDate = currentDate.plusMonths(1)
            } while (currentDate.isMonthAndYearEqualOrSmallerThan(includedLastDate))

        }

        fun forEachMonthWithInitialDateAndRepetition(includedFirstDate: LocalDate, repetition: Int, onEveryMonth: (LocalDate) -> Unit) {
            var currentDate = includedFirstDate

            repeat(repetition) {
                onEveryMonth.invoke(currentDate)
                currentDate = currentDate.plusMonths(1)
            }

        }

//fun getLocalDate(): String = DateTimeFormatter.ofPattern(UTC_TR).format(LocalDate.now())
        /*  fun dateToMonthAndYear(dateLong: String): String {
            val splintedDate = dateLong.split("/")
            return splintedDate[1] + "/" + splintedDate[2]
        }*/
//fun convertToString(day: Int, month: Int, year: Int): String = "$day/$month/$year"
//fun convertToTimeInMillis(dateLong: String): Long = convertToDate(dateLong).time
//fun convertToDate(dateLong: String): java.util.Date = formatter().parse(dateLong)
//fun convertToDate(dateLong:Long):java.util.Date= convertToDate(convertToString(dateLong))

    }

}









