package com.emindev.expensetodolist.income.data.room

import com.emindev.expensetodolist.main.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import java.time.LocalDate

data class IncomeState(
    val incomesMultipleCard: List<Income> = emptyList(),
    val incomesInfinity: List<IncomeModel> = emptyList(),
    val incomesOneCard: List<Income> = emptyList(),
    val id:Long=0,
    val cardId: Long = 0,
    val name: String = "",
    val cardAmount: String = "",
    val latestAmount: String = "",
    val initialDate: LocalDate = DateUtil.localDateNow,
    val currentDate:LocalDate = DateUtil.localDateNow,
    val repeatType: RepeatType = RepeatType.LIMITED,
    val repetition: String = "",
    val isAddingIncome:Boolean = false,
){
    fun toIncome(): Income {
        return Income(id = id,cardId = cardId,name=name, cardAmount =  cardAmount.toFloat(), latestAmount = latestAmount.toFloat(), initialDate = SqlDateUtil.convertDate(initialDate?: DateUtil.localDateNow), currentDate = SqlDateUtil.convertDate(currentDate), deleted = false, cardDeleted = false, repeatType = repeatType, repetition = repetition.toInt())
    }
}