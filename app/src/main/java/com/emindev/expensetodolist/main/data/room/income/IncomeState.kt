package com.emindev.expensetodolist.main.data.room.income

import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import java.time.LocalDate

data class IncomeState(
    val incomes: List<Income> = emptyList(),
    val id:Long=0,
    val cardId: Long = 0,
    val name: String = "",
    val amount: String = "",
    val initialDate: LocalDate = DateUtil.currentDateTime.toLocalDate(),
    val currentDate:LocalDate = DateUtil.currentDateTime.toLocalDate(),
    val isRepeatable: Boolean = false,
    val isAddingIncome:Boolean = false
)