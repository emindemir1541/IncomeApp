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
import androidx.compose.foundation.lazy.LazyListState
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
import com.emindev.expensetodolist.main.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.helper.DateUtil.Companion.convertToString
import com.emindev.expensetodolist.main.common.helper.DateUtil.Companion.isMonthAndYearBiggerThan
import com.emindev.expensetodolist.main.common.constant.Currency
import com.emindev.expensetodolist.main.common.constant.Page
import com.emindev.expensetodolist.income.data.room.Income
import com.emindev.expensetodolist.income.data.room.IncomeEvent
import com.emindev.expensetodolist.income.data.room.IncomeModel
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.ui.component.AlertDialogDelete
import com.emindev.expensetodolist.main.ui.component.TextSizeable
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IncomePage(navController: NavController, mainViewModel: MainViewModel, incomeViewModel: IncomeViewModel, listState: LazyListState, onEvent: (IncomeEvent) -> Unit,) {

    val context = LocalContext.current
    val incomeState by incomeViewModel.state.collectAsState()
    val alertDialogState = rememberUseCaseState(false)
    val selectedDate by mainViewModel.selectedDate.collectAsState()

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState) {


        items(incomeState.incomesInfinity) { income ->
            Box(modifier = Modifier
                .animateItemPlacement(animationSpec = tween(durationMillis = 600))
            ) {
                if (selectedDate.isMonthAndYearBiggerThan(DateUtil.localDateNow))
                    RowIncomeMultipleCard(income, selectedDate)
            }
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
        } // TODO: yükleme animasyonunu düzelt

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
            Spacer(modifier = Modifier.padding(100.dp))
        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
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
                TextSizeable(text = income.name, fontSize = 40)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                TextRemainedDay(income = income)
                TextSizeable(text = income.cardAmount.toString() + Currency.TL, fontSize = 16)  // TODO: handle the cardPassed error
                Text(text = income.currentLocalDate.convertToString(DateUtil.Delimiters.slash))
            }
            Spacer(modifier = Modifier.padding(6.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowIncomeMultipleCard(income: IncomeModel, selectedDate: LocalDate) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, Color.Black),
    ) {

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                TextSizeable(text = income.name, fontSize = 40)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                val date = DateUtil.convertToDate(selectedDate.year, selectedDate.monthValue, income.initialLocalDate.dayOfMonth)
                TextRemainedDay(selectedDate)
                TextSizeable(text = income.latestAmount.toString() + Currency.TL, fontSize = 16)  // TODO: handle the cardPassed error
                Text(text = date.convertToString(DateUtil.Delimiters.slash))
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
                TextSizeable(text = income.name, fontSize = 25)
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

@Composable
private fun TextRemainedDay(selectedDate: LocalDate) {
    Text(
        text = (DateUtil.dayBetweenTwoDate(selectedDate, DateUtil.localDateNow)).toString() + " " + stringResource(R.string.day_remained),
        color = Color.Unspecified, fontWeight = FontWeight.Bold)
}

