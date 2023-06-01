package com.emindev.expensetodolist.main.data.room.income

import androidx.room.Entity
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.isMonthAndYearEqualTo
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.isMonthAndYearSmallerThan
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.localDateNow
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import java.time.LocalDate

@Entity
data class Income(
    val id: Long,
    val cardId: Long,
    val name: String,
    val cardAmount: Float,
    val latestAmount:Float,
    val initialDate: String,
    val currentDate: String,
    val deleted: Boolean,
    val cardDeleted: Boolean,
    var repeatType: RepeatType,
    var repetition: Int
) {

    val toIncomeModel: IncomeModel
        get() = IncomeModel(name = name, latestAmount = latestAmount, initialDate = initialDate, deleted = deleted, repeatType = repeatType, repetition = repetition, id = id)

    val toIncomeCardModel: IncomeCardModel
        get() = IncomeCardModel(id = id, currentDate = currentDate, cardAmount = cardAmount, cardDeleted = cardDeleted, cardId = cardId)

    val initialLocalDate: LocalDate
        get() = SqlDateUtil.convertDate(initialDate)

    val currentLocalDate: LocalDate
        get() = SqlDateUtil.convertDate(currentDate)

    val isMoneyPaid: Boolean
        get() = currentLocalDate <= localDateNow



    val isCardPassed: Boolean
        get() = currentLocalDate.isMonthAndYearSmallerThan(localDateNow)

}