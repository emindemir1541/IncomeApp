package com.emindev.expensetodolist.expense.data.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.main.common.constant.FinanceConstants
import com.emindev.expensetodolist.main.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.helper.DateUtil.Companion.toDateString
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
class ExpenseViewModel(private val dao: ExpenseDao, mainViewModel: MainViewModel) :
    ViewModel() {


    private val _expensesOneCard = mainViewModel.selectedDate.flatMapLatest { selectedDate ->
        dao.getExpenseWithOneCardBySelectedDate(selectedDate.monthValue.toDateString(), selectedDate.year.toString(), SqlDateUtil.dateDelimiter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _expensesMultipleCard = mainViewModel.selectedDate.flatMapLatest { selectedDate ->
        dao.getExpenseWithMultipleCardBySelectedDate(selectedDate.monthValue.toDateString(), selectedDate.year.toDateString(), SqlDateUtil.dateDelimiter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val allExpensesNotDeletedBySelectedDate = mainViewModel.selectedDate.flatMapLatest { selectedDate ->
        dao.getAllExpensesNotDeletedBySelectedDate(selectedDate.monthValue.toDateString(), selectedDate.year.toString(), SqlDateUtil.dateDelimiter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val infinityExpenseModelsNotDeleted = dao.getInfinityExpenseModelsNotDeleted()
    val expenseCardModels = dao.getExpenseCardModels()
    private val _state = MutableStateFlow(ExpenseState())

    val state = combine(_state, _expensesOneCard, _expensesMultipleCard, infinityExpenseModelsNotDeleted, mainViewModel.selectedDate) { state, expensesOneCard, expensesMultipleCard, expenseInfinityModels, _ ->
        state.copy(
            expensesOneCard = expensesOneCard,
            expensesMultipleCard = expensesMultipleCard,
            expenseInfinityModels = expenseInfinityModels
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseState())

    fun onEvent(event: ExpenseEvent) {
        when (event) {
            is ExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    dao.upsert(event.expense.toExpenseModel.copy(deleted = true))
                }
            }

            is ExpenseEvent.DeleteCard -> {
                viewModelScope.launch {
                    dao.upsert(event.expense.toExpenseCardModel.copy(cardDeleted = true))
                }
            }

            ExpenseEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingExpense = false
                    )
                }
            }

            ExpenseEvent.SaveExpense -> {
                //   val id = state.value.id
                //  val cardId = state.value.cardId
                val name = state.value.name
                val latestAmount = state.value.latestAmount
                //  val currentAmount = state.value.currentAmount
                // val initialDate = state.value.initialDate
                val currentDate = state.value.currentDate
                val completed = state.value.completed
                val deleted = state.value.deleted
                val cardDeleted = state.value.cardDeleted
                val repeatType = state.value.repeatType
                val repetition = state.value.repetition
                val expenseType = state.value.expenseType
                val lender = state.value.lender


                val expenseModel = ExpenseModel(
                    name = name,
                    latestAmount = latestAmount.toFloatOrZero(),
                    initialDate = SqlDateUtil.convertDate(currentDate),
                    deleted = deleted,
                    repeatType = repeatType,
                    repetition = repetition.toIntOrZero(),
                    expenseType = expenseType,
                    lender = lender,
                )



                viewModelScope.launch {
                    val getId = dao.upsert(expenseModel)

                    when (repeatType) {
                        RepeatType.ONCE, RepeatType.INFINITY -> {

                            val expenseCardModel = ExpenseCardModel(
                                currentAmount = latestAmount.toFloatOrZero(),
                                currentDate = SqlDateUtil.convertDate(currentDate),
                                cardDeleted = cardDeleted,
                                completed = completed,
                                id = getId
                            )
                            dao.upsert(expenseCardModel)

                        }

                        RepeatType.LIMITED -> {

                            DateUtil.forEachMonthWithInitialDateAndRepetition(currentDate, repetition.toIntOrZero()) { date ->
                                val expenseCardModel = ExpenseCardModel(
                                    currentAmount = latestAmount.toFloatOrZero(),
                                    currentDate = SqlDateUtil.convertDate(date),
                                    cardDeleted = cardDeleted,
                                    completed = completed,
                                    id = getId
                                )
                                dao.upsert(expenseCardModel)
                            }
                        }

                    }
                }


            }

            is ExpenseEvent.SetAmount -> {
                if (event.amount.toFloatOrZero() < FinanceConstants.maxAmount)
                    _state.update {
                        it.copy(
                            currentAmount = event.amount,
                            latestAmount = event.amount
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


            is ExpenseEvent.SetLenderName -> {
                if (event.lender.length < FinanceConstants.maxNameLength)
                    _state.update {
                        it.copy(
                            lender = event.lender
                        )
                    }
            }


            is ExpenseEvent.SetName -> {
                if (event.name.length < FinanceConstants.maxNameLength)
                    _state.update {
                        it.copy(
                            name = event.name
                        )
                    }
            }

            is ExpenseEvent.SetRepetition -> {
                if (event.repetition != null && event.repetition.toIntOrZero() < FinanceConstants.maxRepetitionLength)
                    _state.update {
                        it.copy(
                            repetition = event.repetition.toString()
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


                val id = state.value.id
                val cardId = state.value.cardId
                val name = state.value.name
                val latestAmount = state.value.latestAmount
                val currentAmount = state.value.currentAmount
                val initialDate = state.value.initialDate
                val currentDate = state.value.currentDate
                val completed = state.value.completed
                val deleted = state.value.deleted
                val cardDeleted = state.value.cardDeleted
                val repeatType = state.value.repeatType
                val repetition = state.value.repetition
                val expenseType = state.value.expenseType
                val lender = state.value.lender

                val expenseModel = ExpenseModel(
                    id = id,
                    name = name,
                    latestAmount = latestAmount.toFloatOrZero(),
                    initialDate = SqlDateUtil.convertDate(initialDate),
                    deleted = deleted,
                    repeatType = repeatType,
                    repetition = repetition.toIntOrZero(),
                    expenseType = expenseType,
                    lender = lender,
                )

                val expenseCardModel = ExpenseCardModel(
                    currentAmount = currentAmount.toFloatOrZero(),
                    currentDate = SqlDateUtil.convertDate(currentDate),
                    cardDeleted = cardDeleted,
                    completed = completed,
                    cardId = cardId,
                    id = id
                )


                viewModelScope.launch {
                    dao.upsert(expenseModel)
                    when (repeatType) {
                        RepeatType.ONCE, RepeatType.INFINITY -> {
                            dao.upsert(expenseCardModel)
                        }

                        RepeatType.LIMITED -> {
                            val localDate = DateUtil.localDateNow
                            val filterDate = SqlDateUtil.convertDate(DateUtil.convertToDate(localDate.year, localDate.monthValue, 1))
                            dao.updateAmountOfCardsAfterSpecificDate(id, filterDate, latestAmount.toFloatOrZero())
                            dao.upsert(expenseCardModel)
                        }
                    }
                }
            }

            is ExpenseEvent.SetDate -> {
                _state.update {
                    it.copy(currentDate = event.date)
                }
            }

            is ExpenseEvent.SetExpenseType -> {
                _state.update {
                    it.copy(expenseType = event.type)
                }
            }

            is ExpenseEvent.SetRepeatType -> {
                _state.update {
                    it.copy(repeatType = event.repeatType)
                }
            }

            is ExpenseEvent.Complete -> {
                viewModelScope.launch {
                    dao.upsert(event.expense.toExpenseCardModel)
                }
            }
        }
    }

    fun addExpenseCard(expenseCardModel: ExpenseCardModel) {
        viewModelScope.launch {
            dao.upsert(expenseCardModel)
        }
    }

    fun setState(expense: Expense) {
        _state.update {
            it.copy(id = expense.id, cardId = expense.cardId, name = expense.name, latestAmount = expense.latestAmount.toString(), currentAmount = expense.currentAmount.toString(), initialDate = expense.initialLocalDate, currentDate = expense.currentLocalDate, completed = expense.completed, repeatType = expense.repeatType, cardDeleted = expense.cardDeleted, deleted = expense.deleted, expenseType = expense.expenseType, lender = expense.lender)
        }
    }

    fun clearState() {
        _state.update {
            it.copy(id = 0, cardId = 0, name = "", latestAmount = "", currentAmount = "", initialDate = DateUtil.localDateNow, currentDate = DateUtil.localDateNow, completed = false, cardDeleted = false, repeatType = RepeatType.LIMITED, deleted = false, expenseType = ExpenseType.NEED, isAddingExpense = false, lender = "", repetition = "")
        }
    }

    val stateSaveValid: Boolean
        get() {
            val name = state.value.name
            val latestAmount = state.value.latestAmount
            val repeatType = state.value.repeatType
            val repetition = state.value.repetition
            val expenseType = state.value.expenseType
            val lender = state.value.lender

            return (name.isBlank() || latestAmount.isBlank() || (expenseType == ExpenseType.DEBT && lender.isBlank()) || (repeatType == RepeatType.LIMITED && repetition.isBlank()))

        }

    val stateUpdateValid: Boolean
        get() {
            val name = state.value.name
            val latestAmount = state.value.latestAmount
            val expenseType = state.value.expenseType
            val lender = state.value.lender

            return (name.isBlank() || latestAmount.isBlank() || (expenseType == ExpenseType.DEBT && lender.isBlank()))
        }

}