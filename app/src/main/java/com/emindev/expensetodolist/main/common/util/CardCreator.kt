package com.emindev.expensetodolist.main.common.util

import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.addLog
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.expensetodolist.income.data.room.IncomeCardModel
import com.emindev.expensetodolist.income.data.room.IncomeModel
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class CardCreator(private val mainViewModel: MainViewModel, private val incomeViewModel: IncomeViewModel, private val expenseViewModel: ExpenseViewModel) {

    private val coroutineErrorHandler = CoroutineExceptionHandler { _, throwable ->
        addLog("CardCreator", throwable.localizedMessage, "Error occurred while creating income card", "CardCreator")
        mainViewModel.cardCreatingFinished
    }

    val runCreator = CoroutineScope(Dispatchers.IO + coroutineErrorHandler).launch {
        withTimeoutOrNull(15000L) {
            mainViewModel.cardCreatingStarted
            coroutineScope {
                joinAll(createIncomeCards(),createExpenseCars())
                mainViewModel.cardCreatingFinished
            }


        } ?: run {
            mainViewModel.cardCreatingFinished
            test = "creator timeout"
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

    }


}