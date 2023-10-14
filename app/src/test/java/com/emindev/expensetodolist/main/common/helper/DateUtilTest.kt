package com.emindev.expensetodolist.main.common.helper

import com.emindev.expensetodolist.main.common.helper.DateUtil.Companion.dayOrMonthToValidString
import org.junit.Test
import kotlin.random.Random

class DateUtilTest {

    @Test
    fun `toDateString max value control`() {
        assert(Int.MAX_VALUE.dayOrMonthToValidString() == "2147483647")
    }

    @Test
    fun `toDateString min value control`() {
        assert(Int.MIN_VALUE.dayOrMonthToValidString() == "01") { Int.MIN_VALUE.dayOrMonthToValidString() }
    }

    @Test
    fun `toDateString repeat value control`() {
        repeat(9) {
            assert((it + 1).dayOrMonthToValidString() == "0${it + 1}") { it.dayOrMonthToValidString() }
        }
    }

    @Test
    fun `splitDate slash`() {
        val dateList = listOf(Random.nextInt(),Random.nextInt(),Random.nextInt())
        DateUtil.splitDate("${dateList[0]}/${dateList[1]}/${dateList[2]}", DateUtil.Delimiters.slash).forEach {
            assert(dateList.contains(it.toInt()))
        }
    }

    @Test
    fun `convertToString() Int to string, min value test`(){

    }
}