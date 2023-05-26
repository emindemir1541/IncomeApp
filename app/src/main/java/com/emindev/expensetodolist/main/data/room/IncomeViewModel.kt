package com.emindev.expensetodolist.main.data.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class IncomeViewModel(private val dao: IncomeDao) : ViewModel() {

    private val _selectedDateState = MutableStateFlow(DateUtil.currentDateTime)

    private val _incomes = _selectedDateState.flatMapLatest { selectedDate ->
        dao.readSelectedData(selectedDate.monthValue, selectedDate.year)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(IncomeState())

    val state = combine(_state, _incomes, _selectedDateState) { state, incomes, selectedDate ->
        state.copy(
            incomes = incomes,
            selectedDate = selectedDate
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), IncomeState())

    fun onEvent(event: IncomeEvent) {
        when (event) {
            is IncomeEvent.DeleteIncome -> {
                viewModelScope.launch {
                    val incomeList = state.value.incomes
                    dao.delete(event.income.id)
                }
            }

            IncomeEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingIncome = false
                    )
                }
            }

            IncomeEvent.SaveIncome -> {
                val name = state.value.name
                val amount = state.value.amount
                val startedDate = state.value.startedDate
                val day = state.value.day
                val month = state.value.month
                val year = state.value.year
                val deleted = state.value.deleted
                val isRepeatable = state.value.isRepeatable
                val cardId = state.value.cardId

                if (name.isBlank() || amount.isBlank()) {
                    return
                }

                val income = Income(
                    name = name,
                    amount = amount.toFloat(),
                    startedDateLong = startedDate,
                    day = day,
                    month = month,
                    year = year,
                    deleted = deleted,
                    isRepeatable = isRepeatable,
                    cardId = cardId
                )
                viewModelScope.launch {
                    dao.upsert(income)
                }

            }

            is IncomeEvent.SetAmount -> {
                _state.update {
                    it.copy(
                        amount = event.amount.toString()
                    )
                }
            }

            is IncomeEvent.SetCardId -> {
                _state.update {
                    it.copy(
                        cardId = event.cardId
                    )
                }
            }

            is IncomeEvent.SetDate -> {
                _selectedDateState.value = event.selectedDate
            }

            is IncomeEvent.SetDay -> {
                _state.update {
                    it.copy(
                        day = event.day
                    )
                }
            }

            is IncomeEvent.SetDeleted -> {
                _state.update {
                    it.copy(
                        deleted = event.deleted
                    )
                }
            }

            is IncomeEvent.SetIsRepeatable -> {
                _state.update {
                    it.copy(
                        isRepeatable = event.isRepeatable
                    )
                }
            }

            is IncomeEvent.SetMonth -> {
                _state.update {
                    it.copy(
                        month = event.month
                    )
                }
            }

            is IncomeEvent.SetName -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            is IncomeEvent.SetStartedDate -> {
                _state.update {
                    it.copy(
                        startedDate = event.startedDate
                    )
                }
            }

            is IncomeEvent.SetYear -> {
                _state.update {
                    it.copy(
                        year = event.year
                    )
                }
            }

            IncomeEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingIncome = true
                    )
                }
            }

            IncomeEvent.UpdateIncome -> {
                val name = state.value.name
                val amount = state.value.amount
                val startedDate = state.value.startedDate
                val day = state.value.day
                val month = state.value.month
                val year = state.value.year
                val deleted = state.value.deleted
                val isRepeatable = state.value.isRepeatable
                val cardId = state.value.cardId

                val incomeList = state.value.incomes

                if (name.isBlank() || amount.isBlank()) {
                    return
                }

                val income = Income(
                    name = name,
                    amount = amount.toFloat(),
                    startedDateLong = startedDate,
                    day = day,
                    month = month,
                    year = year,
                    deleted = deleted,
                    isRepeatable = isRepeatable,
                    cardId = cardId
                )
                viewModelScope.launch {
                    dao.upsert(income)
                }
            }
        }
    }

}

