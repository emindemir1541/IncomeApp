package com.emindev.expensetodolist.income.data.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.toDateString
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import com.emindev.expensetodolist.main.common.util.toFloatOrZero
import com.emindev.expensetodolist.main.common.util.toIntOrZero
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class IncomeViewModel(private val dao: IncomeDao, private val mainViewModel: MainViewModel) :
    ViewModel() {

    val incomeInfinityModelsNotDeleted = dao.getInfinityIncomeModelsNotDeleted()
    val incomeModels = dao.getIncomeModels()
    val incomeCardModelsNotDeleted = dao.getIncomeCardModelsNotDeleted()
    val incomeCardModels = dao.getIncomeCardModels()
    val incomeInfinityModels = dao.getIncomeInfinityModels()


    private val _incomesMultipleCard = mainViewModel.selectedDate.flatMapLatest { selectedDate ->
        dao.getIncomesWithMultipleCardBySelectedDate(selectedDate.monthValue.toDateString(), selectedDate.year.toString(), SqlDateUtil.dateDelimiter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private val _incomesOneCard = mainViewModel.selectedDate.flatMapLatest { selectedDate ->
        dao.getIncomesWithOneCardBySelectedDate(selectedDate.monthValue.toDateString(), selectedDate.year.toString(), SqlDateUtil.dateDelimiter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(IncomeState())
    val state = combine(_state, _incomesMultipleCard, incomeInfinityModels, _incomesOneCard, mainViewModel.selectedDate) { state, incomesMultipleCard, _incomeInfinityModels, incomesOneCard, _ ->
        state.copy(
            incomesMultipleCard = incomesMultipleCard,
            incomesInfinity = _incomeInfinityModels,
            incomesOneCard = incomesOneCard,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), IncomeState())


    fun cardsByIncome(income: Income) = dao.getCardsByIncome(income.id)

    fun addIncomeCard(incomeCardModel: IncomeCardModel) {
        viewModelScope.launch {
            dao.upsert(incomeCardModel)
        }
    }

    fun onEvent(event: IncomeEvent) {
        when (event) {
            is IncomeEvent.DeleteIncome -> {
                viewModelScope.launch {
                    dao.upsert(event.income.toIncomeModel.copy(deleted = true))
                }
            }

            is IncomeEvent.DeleteCard -> {
                viewModelScope.launch {
                    dao.upsert(event.income.toIncomeCardModel.copy(cardDeleted = true))
                }
            }

            IncomeEvent.HideDialog -> {
                clearState()
            }

            IncomeEvent.SaveIncome -> {

                val name = state.value.name
                //val cardAmount = state.value.cardAmount
                val latestAmount = state.value.latestAmount
                // val initialDate = state.value.initialDate
                val currentDate = state.value.currentDate
                val repeatType = state.value.repeatType
                val repetition = state.value.repetition

                if (name.isBlank() || latestAmount.isBlank() || (repeatType == RepeatType.LIMITED && repetition.isBlank())) {
                    // TODO:  edit this place
                    return
                }
                val incomeModel = IncomeModel(
                    name = name,
                    latestAmount = latestAmount.toFloatOrZero(),
                    initialDate = SqlDateUtil.convertDate(currentDate),
                    deleted = false,
                    repeatType = repeatType,
                    repetition = repetition.toIntOrZero()
                )
                viewModelScope.launch {
                    val getId = dao.upsert(incomeModel)

                    when (repeatType) {
                        RepeatType.ONCE, RepeatType.INFINITY -> {

                            val incomeCardModel = IncomeCardModel(
                                id = getId,
                                currentDate = SqlDateUtil.convertDate(currentDate),
                                cardAmount = latestAmount.toFloatOrZero(),
                                cardDeleted = false,
                            )
                            dao.upsert(incomeCardModel)
                        }

                        RepeatType.LIMITED -> {
                            DateUtil.forEachMonthWithInitialDateAndRepetition(currentDate, repetition.toIntOrZero()) { date ->
                                val incomeCardModel = IncomeCardModel(
                                    id = getId,
                                    currentDate = SqlDateUtil.convertDate(date),
                                    cardAmount = latestAmount.toFloat(),
                                    cardDeleted = false,
                                )
                                dao.upsert(incomeCardModel)
                            }
                        }
                    }
                }


                clearState()

            }

            IncomeEvent.UpdateIncome -> {
                val id = state.value.id
                val cardId = state.value.cardId
                val name = state.value.name
                //val cardAmount = state.value.cardAmount
                val latestAmount = state.value.latestAmount
                val initialDate = state.value.initialDate
                val currentDate = state.value.currentDate
                val repeatType = state.value.repeatType
                val repetition = state.value.repetition

                if (name.isBlank() || latestAmount.isBlank() || (repeatType == RepeatType.LIMITED && repetition.isBlank())) {
                    return
                }

                val incomeModel = IncomeModel(
                    id = id,
                    name = name,
                    latestAmount = latestAmount.toFloat(),
                    initialDate = SqlDateUtil.convertDate(initialDate),
                    deleted = false,
                    repeatType = repeatType,
                    repetition = repetition.toIntOrNull() ?: 0
                )
                val incomeCardModel = IncomeCardModel(
                    id = id,
                    cardId = cardId,
                    currentDate = SqlDateUtil.convertDate(currentDate),
                    cardAmount = latestAmount.toFloat(),
                    cardDeleted = false
                )
                viewModelScope.launch {
                    dao.upsert(incomeModel)
                    when (repeatType) {
                        RepeatType.ONCE, RepeatType.INFINITY -> {
                            dao.upsert(incomeCardModel)
                        }

                        RepeatType.LIMITED -> {
                            val localDate = DateUtil.localDateNow
                            val filterDate = SqlDateUtil.convertDate(DateUtil.convertToDate(localDate.year, localDate.monthValue, 1))
                            dao.updateAmountOfCardsAfterSpecificDate(id, filterDate, latestAmount.toFloat())

                        }
                    }


                }
                clearState()
            }

            is IncomeEvent.SetAmount -> {
                _state.update {
                    it.copy(
                        latestAmount = event.amount,
                        cardAmount = event.amount
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


            is IncomeEvent.SetRepeatType -> {
                _state.update {
                    it.copy(
                        repeatType = event.repeatType
                    )
                }
            }

            is IncomeEvent.SetRepetition -> {
                _state.update {
                    it.copy(
                        repetition = event.repetition
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


            is IncomeEvent.SetDate -> {
                _state.update {
                    it.copy(
                        currentDate = event.currentDate
                    )
                }
            }

        }
    }


    fun clearState() {
        _state.update {
            it.copy(id = 0, cardId = 0, name = "", latestAmount = "", cardAmount = "", initialDate = DateUtil.localDateNow, currentDate = DateUtil.localDateNow, repeatType = RepeatType.LIMITED, repetition = "", isAddingIncome = false)
        }
    }

    fun setState(income: Income) {
        _state.update {
            it.copy(
                id = income.id, cardId = income.cardId, latestAmount = income.latestAmount.toString(), cardAmount = income.cardAmount.toString(), initialDate = income.initialLocalDate, currentDate = income.currentLocalDate, repeatType = income.repeatType, repetition = income.repetition.toString(), name = income.name,
            )
        }
    }

}

