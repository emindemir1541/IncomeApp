package com.emindev.expensetodolist.main.data.room

import java.time.LocalDateTime

sealed interface IncomeEvent {
    object SaveIncome:IncomeEvent
    object UpdateIncome:IncomeEvent
    data class SetName(val name:String):IncomeEvent
    data class SetAmount(val amount:Float):IncomeEvent
    data class SetStartedDate(val startedDate:Long):IncomeEvent
    data class SetDay(val day:Int):IncomeEvent
    data class SetMonth(val month:Int):IncomeEvent
    data class SetYear(val year:Int):IncomeEvent
    data class SetDeleted(val deleted:Boolean):IncomeEvent
    data class SetIsRepeatable(val isRepeatable:Boolean):IncomeEvent
    data class SetCardId(val cardId:Long):IncomeEvent
    object ShowDialog:IncomeEvent
    object HideDialog:IncomeEvent
    data class DeleteIncome(val income:Income):IncomeEvent
}