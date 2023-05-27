package com.emindev.expensetodolist.main.data.room.income

import java.time.LocalDate

sealed interface IncomeEvent {
    object SaveIncome: IncomeEvent
    object UpdateIncome: IncomeEvent
    data class SetName(val name:String): IncomeEvent
    data class SetAmount(val amount:Float): IncomeEvent
    data class SetInitialDate(val initialDate:LocalDate): IncomeEvent
    data class SetCurrentDate(val currentDate:LocalDate): IncomeEvent
    data class SetIsRepeatable(val isRepeatable:Boolean): IncomeEvent
    data class SetCardId(val cardId:Long): IncomeEvent
    object CreateCard: IncomeEvent
    object ShowDialog: IncomeEvent
    object HideDialog: IncomeEvent
    data class DeleteIncome(val income: Income): IncomeEvent
}