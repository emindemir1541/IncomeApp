package com.emindev.expensetodolist.main.common.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.addLog
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.income.data.room.IncomeCardModel
import com.emindev.expensetodolist.income.data.room.IncomeModel
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import java.lang.Exception

@SuppressLint("SuspiciousIndentation")
@Composable
fun CardCreator(incomeViewModel: IncomeViewModel) {

    //infinity income card creator
    // TODO: check if this month already created, you can save this settings to datastore app settings
    // TODO: ileride gösterilmesi gereken kartlar için yansıma kullan, bu yansıma expense de tamamlandıysa kaydet


    val incomeModelListFlow = incomeViewModel.incomeModelsNotDeleted.collectAsState(initial = emptyList())
    val incomeCardModelListFlow = incomeViewModel.incomeCardModels.collectAsState(initial = emptyList())


    if (incomeModelListFlow.value.isNotEmpty() && incomeCardModelListFlow.value.isNotEmpty()) {
        // TODO: make loading view for no one add or change to database


        val incomeModelList = incomeModelListFlow.value
        val incomeCardModelList = incomeCardModelListFlow.value

        incomeModelList.filter { income: IncomeModel -> income.repeatType == RepeatType.INFINITY }.forEach { incomeModel ->
            try {
                DateUtil.forEachMonthBetweenTwoDate(incomeModel.initialLocalDate,DateUtil.localDateNow) {currentDate->
                    val incomeCard = incomeCardModelList.find { incomeCardModel -> incomeCardModel.id == incomeModel.id && incomeCardModel.currentLocalDate == currentDate }
                    if (incomeCard == null) {
                        val incomeCardModel = IncomeCardModel(id = incomeModel.id,currentDate= SqlDateUtil.convertDate(currentDate), cardAmount = incomeModel.latestAmount, cardDeleted = false)
                        incomeViewModel.addIncomeCard(incomeCardModel)
                    }

                }
            } catch (e: Exception) {
                addLog("CardCreator", e.localizedMessage, "Error occurred while creating card", "CardCreator.kt")
            }
        }


    }


}