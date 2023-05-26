package com.emindev.expensetodolist.main.data.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModel(private val dao: ExpenseDao) : ViewModel() {

    private val _selectedDateState = MutableStateFlow(DateUtil.currentDateTime)

    private val _expenses = _selectedDateState.flatMapLatest { selectedDate ->
        dao.readSelectedData(selectedDate.monthValue, selectedDate.year)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ExpenseState())

    val state = combine(_state, _expenses, _selectedDateState) { state, expenses, selectedDate ->
        state.copy(
            expenses = expenses,
            selectedDate = selectedDate
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseState())

    fun onEvent(event: ExpenseEvent) {
        when (event) {
            is ExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    dao.delete(event.expense)
                }
            }

           is ExpenseEvent.SetDate -> {
                _selectedDateState.value = event.selectedDate
            }

            ExpenseEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingExpense = false
                    )
                }
            }

            ExpenseEvent.SaveExpense -> {
                val name = state.value.name
                val amount = state.value.amount
                val startedDate = state.value.startedDate
                val completed = state.value.completed
                val lender = state.value.lender
                val repetition = state.value.repetition
                val repetitionSituation = state.value.repetitionSituation
                val deleted = state.value.deleted
                val type = state.value.type
                val day = state.value.day
                val month = state.value.month
                val year = state.value.year
                val cardId = state.value.cardId

                if (name.isBlank() || amount.isBlank() || (type == ExpenseType.DEBT && lender.isBlank()) || repetition.isBlank()) {
                    return
                }

                val expense = Expense(
                    name = name,
                    amount = amount.toFloat(),
                    startedDateLong = startedDate,
                    completed = completed,
                    lender = lender,
                    repetition = if (repetitionSituation) repetition.toInt() else null,
                    deleted = deleted,
                    type = type,
                    day = day,
                    month = month,
                    year = year,
                    cardId = cardId
                )

                viewModelScope.launch {
                    dao.upsert(expense)
                }
            }

            is ExpenseEvent.SetAmount -> {
                _state.update {
                    it.copy(
                        amount = event.amount.toString()
                    )
                }
            }

            is ExpenseEvent.SetCardId -> {
                _state.update {
                    it.copy(
                        cardId = event.cardId
                    )
                }
            }

            is ExpenseEvent.SetCompleted -> {
                _state.update {
                    it.copy(
                        completed = event.completed
                    )
                }
            }

            is ExpenseEvent.SetDay -> {
                _state.update {
                    it.copy(
                        day = event.day
                    )
                }
            }

            is ExpenseEvent.SetDeleted -> {
                _state.update {
                    it.copy(
                        deleted = event.deleted
                    )
                }
            }

            is ExpenseEvent.SetExpenseType -> {
                _state.update {
                    it.copy(
                        type = event.type
                    )
                }
            }

            is ExpenseEvent.SetLenderName -> {
                _state.update {
                    it.copy(
                        lender = event.lender ?: ""
                    )
                }
            }

            is ExpenseEvent.SetMonth -> {
                _state.update {
                    it.copy(
                        month = event.month
                    )
                }
            }

            is ExpenseEvent.SetName -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            is ExpenseEvent.SetRepetition -> {
                _state.update {
                    it.copy(
                        repetition = if (event.repetition != null) event.repetition.toString() else ""
                    )
                }
            }

            is ExpenseEvent.SetStartedDate -> {
                _state.update {
                    it.copy(
                        startedDate = event.startedDate
                    )
                }
            }

            is ExpenseEvent.SetYear -> {
                _state.update {
                    it.copy(
                        year = event.year
                    )
                }
            }

            ExpenseEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingExpense = true
                    )
                }
            }

            ExpenseEvent.UpdateExpense -> {
                val name = state.value.name
                val amount = state.value.amount
                val startedDate = state.value.startedDate
                val completed = state.value.completed
                val lender = state.value.lender
                val repetition = state.value.repetition
                val repetitionSituation = state.value.repetitionSituation
                val deleted = state.value.deleted
                val type = state.value.type
                val day = state.value.day
                val month = state.value.month
                val year = state.value.year
                val cardId = state.value.cardId

                val expenseList = state.value.expenses

                if (name.isBlank() || amount.isBlank() || (type == ExpenseType.DEBT && lender.isBlank()) || repetition.isBlank()) {
                    return
                }

                val expense = Expense(
                    name = name,
                    amount = amount.toFloat(),
                    startedDateLong = startedDate,
                    completed = completed,
                    lender = lender,
                    repetition = if (repetitionSituation) repetition.toInt() else null,
                    deleted = deleted,
                    type = type,
                    day = day,
                    month = month,
                    year = year,
                    cardId = cardId
                )

                viewModelScope.launch {
                    dao.upsert(expense)
                }
            }
        }
    }

}