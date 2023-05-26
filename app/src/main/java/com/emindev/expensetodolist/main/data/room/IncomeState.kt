package com.emindev.expensetodolist.main.data.room

import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import java.time.LocalDateTime

data class IncomeState(
    val incomes: List<Income> = emptyList(),
    val selectedDate: LocalDateTime = DateUtil.currentDateTime,
    val name: String = "",
    val amount: String = "",
    val startedDate: Long = 0,
    val day: Int = 0,
    val month: Int = 0,
    val year: Int = 0,
    val deleted: Boolean = false,
    val isRepeatable: Boolean = false,
    val cardId: Long = 0,
    val isAddingIncome:Boolean = false
)