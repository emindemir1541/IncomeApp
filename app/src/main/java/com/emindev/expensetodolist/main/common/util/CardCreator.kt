package com.emindev.expensetodolist.main.common.util

import com.emindev.expensetodolist.expense.data.room.ExpenseCardModel
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.main.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.helper.addLog
import com.emindev.expensetodolist.income.data.room.IncomeCardModel
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class CardCreator(private val mainViewModel: MainViewModel, private val incomeViewModel: IncomeViewModel, private val expenseViewModel: ExpenseViewModel) {

    private val coroutineErrorHandler = CoroutineExceptionHandler { _, throwable ->
        addLog("CardCreator", throwable.localizedMessage, "Error occurred while creating income card", "CardCreator")
        mainViewModel.cardCreatingFinished
    }

    val runCreator = CoroutineScope(Dispatchers.IO + coroutineErrorHandler).launch {
        withTimeoutOrNull(15000L) {
            mainViewModel.cardCreatingStarted
            coroutineScope {
                joinAll(createIncomeCards(), createExpenseCars())
                mainViewModel.cardCreatingFinished
            }


        } ?: run {
            mainViewModel.cardCreatingFinished
        }

    }

    private fun CoroutineScope.createIncomeCards() = launch {

        val incomeModelList = incomeViewModel.incomeInfinityModelsNotDeleted.firstOrNull()
        val incomeCardModelList = incomeViewModel.incomeCardModels.firstOrNull()

        if (!incomeModelList.isNullOrEmpty() && !incomeCardModelList.isNullOrEmpty()) {
            incomeModelList.forEach { incomeModel ->
                DateUtil.forEachMonthBetweenTwoDate(incomeModel.initialLocalDate, DateUtil.localDateNow) { currentDate ->
                    val incomeCard = incomeCardModelList.find { incomeCardModel -> incomeCardModel.id == incomeModel.id && incomeCardModel.currentLocalDate == currentDate }
                    if (incomeCard == null) {
                        val incomeCardModel = IncomeCardModel(id = incomeModel.id, currentDate = SqlDateUtil.convertDate(currentDate), cardAmount = incomeModel.latestAmount, cardDeleted = false)
                        incomeViewModel.addIncomeCard(incomeCardModel)
                    }
                }
            }
        }
    }

    private fun CoroutineScope.createExpenseCars() = launch {
        val expenseModelList = expenseViewModel.infinityExpenseModelsNotDeleted.firstOrNull()
        val expenseCardModelList = expenseViewModel.expenseCardModels.firstOrNull()

        if (!expenseModelList.isNullOrEmpty() && !expenseCardModelList.isNullOrEmpty()) {
            expenseModelList.forEach{expenseModel->
                DateUtil.forEachMonthBetweenTwoDate(expenseModel.initialLocalDate, DateUtil.localDateNow) { currentDate->
                    val expenseCard = expenseCardModelList.find { expenseCardModel -> expenseCardModel.id == expenseModel.id && expenseCardModel.currentLocalDate == currentDate }
                    if (expenseCard == null) {
                        val expenseCardModel = ExpenseCardModel(id = expenseModel.id, currentDate = SqlDateUtil.convertDate(currentDate), currentAmount = expenseModel.latestAmount, cardDeleted = false, completed = false)
                        expenseViewModel.addExpenseCard(expenseCardModel)
                    }
                }
            }
        }
    }


}

