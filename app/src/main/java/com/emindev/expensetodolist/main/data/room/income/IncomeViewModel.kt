package com.emindev.expensetodolist.main.data.room.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class IncomeViewModel(private val dao: IncomeDao, private val mainViewModel: MainViewModel) :
    ViewModel() {

    private val _incomes = mainViewModel.selectedDate.flatMapLatest { selectedDate ->
        dao.getIncomesBySelectedDate(selectedDate.monthValue.toString(), selectedDate.year.toString())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val allIncomeDao = dao.readAllData()
    private val _state = MutableStateFlow(IncomeState())

    val state = combine(_state, _incomes, mainViewModel.selectedDate) { state, incomes, _ ->
        state.copy(
            incomes = incomes,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), IncomeState())


    fun cardsByIncome(income:Income) = dao.getCardsByIncome(income.id)

    fun addIncomeCard(income: Income, currentDate: LocalDate) {
        // TODO: add card 
        // TODO: add card if repetable loop
    }

    fun onEvent(event: IncomeEvent) {
        when (event) {
            is IncomeEvent.DeleteIncome -> {
                viewModelScope.launch {
                  dao.upsert(event.income.copy(deleted = true).toIncomeModel)
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
                val initialDate = state.value.initialDate
                val currentDate = state.value.currentDate
                val isRepeatable = state.value.isRepeatable

                if (name.isBlank() || amount.isBlank()) {
                    return
                }

                val incomeModel = IncomeModel(
                    name = name,
                    latestAmount = amount.toFloat(),
                    initialDate = DateUtil.convertToStringTypeSql(initialDate),
                    deleted = false,
                    isRepeatable = isRepeatable,
                )
                viewModelScope.launch {
                    val id = dao.upsert(incomeModel)
                    val incomeCardModel = IncomeCardModel(
                        id  = id,
                        currentDate = DateUtil.convertToStringTypeSql(currentDate),
                        amount = amount.toFloat(),
                    )
                    dao.upsert(incomeCardModel)
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




            is IncomeEvent.SetIsRepeatable -> {
                _state.update {
                    it.copy(
                        isRepeatable = event.isRepeatable
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


            IncomeEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingIncome = true
                    )
                }
            }

            IncomeEvent.UpdateIncome -> {
        /*        val name = state.value.name
                val amount = state.value.amount
                val initialDate = state.value.initialDate
                val currentDate = state.value.currentDate
                val deleted = state.value.deleted
                val isRepeatable = state.value.isRepeatable
                val cardId = state.value.cardId

                if (name.isBlank() || amount.isBlank()) {
                    return
                }

                val income = Income(
                    name = name,
                    latestAmount = amount.toFloat(),
                    _initialDate = DateUtil.convertToString(initialDate),
                    _currentDate = DateUtil.convertToString(currentDate),
                    deleted = deleted,
                    isRepeatable = isRepeatable,
                    cardId = cardId // TODO: use card id
                )
                viewModelScope.launch {
                    dao.upsert(income)
                }*/
            }

            is IncomeEvent.SetCurrentDate -> {
                _state.update {
                    it.copy(
                        currentDate = event.currentDate
                    )
                }
            }

            is IncomeEvent.SetInitialDate -> {
                _state.update {
                    it.copy(
                        initialDate = event.initialDate
                    )
                }
            }

            IncomeEvent.CreateCard -> {

                // TODO: when app started, create cards if doesnt exist
            }
        }
    }


    init {
       /* allIncomeDao.combine(uniqueCardIncomeDao) { allIncome, uniqueIncome ->
            allIncomes = allIncome
            uniqueIncomeCards = uniqueIncome
        }.launchIn(viewModelScope)*/
    }

}

