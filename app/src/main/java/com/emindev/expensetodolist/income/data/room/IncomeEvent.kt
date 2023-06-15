package com.emindev.expensetodolist.income.data.room

import com.emindev.expensetodolist.main.common.constant.RepeatType
import java.time.LocalDate

sealed interface IncomeEvent {
    object SaveIncome: IncomeEvent
    object UpdateIncome: IncomeEvent
    data class SetName(val name:String): IncomeEvent
    data class SetAmount(val amount:String): IncomeEvent
    data class SetDate(val currentDate:LocalDate): IncomeEvent
    data class SetRepeatType(val repeatType: RepeatType): IncomeEvent
    data class SetRepetition(val repetition: String): IncomeEvent
    data class SetCardId(val cardId:Long): IncomeEvent
    object ShowDialog: IncomeEvent
    object HideDialog: IncomeEvent
    data class DeleteIncome(val income: Income): IncomeEvent
    data class DeleteCard(val income: Income): IncomeEvent
}