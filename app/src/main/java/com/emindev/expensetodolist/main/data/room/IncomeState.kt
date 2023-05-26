package com.emindev.expensetodolist.main.data.room

import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import java.time.LocalDate
import java.time.LocalDateTime

data class IncomeState(
    val incomes: List<Income> = emptyList(),
    val name: String = "",
    val amount: String = "",
    val initialDate: LocalDate = DateUtil.currentDateTime.toLocalDate(),
    val currentDate:LocalDate = DateUtil.currentDateTime.toLocalDate(),
    val deleted: Boolean = false,
    val isRepeatable: Boolean = false,
    val cardId: Long = 0,
    val isAddingIncome:Boolean = false
)