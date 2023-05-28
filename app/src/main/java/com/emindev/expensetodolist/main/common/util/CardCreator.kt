package com.emindev.expensetodolist.main.common.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.isMonthAndYearEqualOrSmallerThan
import com.emindev.expensetodolist.helperlibrary.common.helper.addLog
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.expensetodolist.main.data.room.income.IncomeCardModel
import com.emindev.expensetodolist.main.data.room.income.IncomeModel
import com.emindev.expensetodolist.main.data.room.income.IncomeViewModel
import java.lang.Exception

@SuppressLint("SuspiciousIndentation")
@Composable
fun CardCreator(incomeViewModel: IncomeViewModel) {

    //infinity income card creator
    // TODO: check if this month already created, you can save this settings to datastore app settings
    // TODO: ileride gösterilmesi gereken kartlar için yansıma kullan, bu yansıma expense de tamamlandıysa kaydet


    val incomeModelListFlow = incomeViewModel.incomeModels.collectAsState(initial = emptyList())
    val incomeCardModelListFlow = incomeViewModel.incomeCardModels.collectAsState(initial = emptyList())


    if (incomeModelListFlow.value.isNotEmpty() && incomeCardModelListFlow.value.isNotEmpty()) {
        // TODO: make loading view for no one add or change to database


        val incomeModelList = incomeModelListFlow.value
        val incomeCardModelList = incomeCardModelListFlow.value

        incomeModelList.filter { income: IncomeModel -> income.isRepeatable }.forEach { incomeModel ->
            try {
                var dateCounter = incomeModel.initialLocalDate
                while (dateCounter.isMonthAndYearEqualOrSmallerThan(DateUtil.localDateNow)) {
                    val incomeCard = incomeCardModelList.find { incomeCardModel -> incomeCardModel.id == incomeModel.id && incomeCardModel.currentLocalDate == dateCounter }
                    if (incomeCard == null) {
                        val incomeCardModel = IncomeCardModel(incomeModel.id, SqlDateUtil.convertDate(dateCounter), incomeModel.latestAmount)
                        incomeViewModel.addIncomeCard(incomeCardModel)
                    }
                    dateCounter = dateCounter.plusMonths(1)

                }
            } catch (e: Exception) {
                addLog("CardCreator", e.localizedMessage, "Error occurred while creating card", "CardCreator.kt")
            }
        }


    }


}