@file:OptIn(ExperimentalFoundationApi::class)

package com.emindev.expensetodolist.income.ui.pages

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.convertToString
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.isMonthAndYearSmallerThan
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.expensetodolist.main.common.constant.Currency
import com.emindev.expensetodolist.main.common.constant.Page
import com.emindev.expensetodolist.main.common.util.CardCreator
import com.emindev.expensetodolist.main.data.room.income.Income
import com.emindev.expensetodolist.main.data.room.income.IncomeEvent
import com.emindev.expensetodolist.main.data.room.income.IncomeViewModel
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.ui.component.AlertDialogDelete
import com.emindev.expensetodolist.main.ui.component.ButtonAdd
import com.emindev.expensetodolist.main.ui.component.DateRow
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IncomePage(navController: NavController, mainViewModel: MainViewModel, incomeViewModel: IncomeViewModel, onEvent: (IncomeEvent) -> Unit) {

    val context = LocalContext.current

    val incomeState by incomeViewModel.state.collectAsState()
    val alertDialogState = rememberUseCaseState(false)


    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

        item {
            DateRow(modifier = Modifier.padding(vertical = 16.dp), mainViewModel = mainViewModel)
        }


        items(incomeState.incomesMultipleCard) { income ->
            AlertDialogDelete(onDeleteCardClick = { onEvent(IncomeEvent.DeleteCard(income)) }, onDeleteAllClick = { onEvent(IncomeEvent.DeleteIncome(income)) }, alertDialogState)

            Box(modifier = Modifier
                .animateItemPlacement(animationSpec = tween(durationMillis = 600))
            ) {
                RowIncomeMultipleCard(income) {
                    if (income.isCardPassed) {
                        alertDialogState.show()
                    }
                    else {
                        incomeViewModel.setState(income)
                        navController.navigate(Page.IncomeUpdate.route)
                    }
                }
            }

        }

        items(incomeState.incomesOneCard) { income ->
            Box(modifier = Modifier
                .animateItemPlacement(animationSpec = tween(durationMillis = 600))
            ) {
                RowIncomeOneCard(income) {
                    incomeViewModel.setState(income)
                    navController.navigate(Page.IncomeUpdate.route)
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth()) {

                ButtonAdd {
                    navController.navigate(Page.IncomeAdd.route)
                }
            }

        }
    }
}

@Composable
fun RowIncomeMultipleCard(income: Income, onLongClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, Color.Black),
    ) {

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(text = income.name, fontSize = 30.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                TextRemainedDay(income = income)
                Text(text = income.cardAmount.toString() + Currency.TL)  // TODO: handle the cardPassed error
                Text(text = income.currentLocalDate.convertToString(DateUtil.Delimiters.slash))
            }
            Spacer(modifier = Modifier.padding(6.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RowIncomeOneCard(income: Income, onLongClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, Color.Black),
    ) {

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                Text(text = income.name, fontSize = 25.sp) // TODO: title sınırı, belli bir değerden sonra yazı boyutu küçülsün 
                Text(text = income.currentLocalDate.convertToString(DateUtil.Delimiters.slash)) // TODO: bunu tam ortala
                Text(text = if (income.isCardPassed) income.cardAmount.toString() else income.latestAmount.toString() + Currency.TL)  // TODO: handle the cardPassed error

            }

        }
    }
}

@Composable
private fun TextRemainedDay(income: Income) {
    Text(
        text = if (income.isMoneyPaid) stringResource(R.string.paid) else (DateUtil.dayBetweenTwoDate(income.currentLocalDate, DateUtil.localDateNow)).toString() + " " + stringResource(R.string.day_remained),
        color = if (income.isMoneyPaid) Color.Green else Color.Unspecified, fontWeight = FontWeight.Bold)
}

