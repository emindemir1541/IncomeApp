package com.emindev.expensetodolist.main.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.common.calculation.Calculator
import kotlinx.coroutines.flow.StateFlow

@Preview
@Composable
fun MainPage(
    incomeViewModel: IncomeViewModel, expenseViewModel: ExpenseViewModel
) {
    val context = LocalContext.current
    val incomeListData = incomeViewModel.readAllData.observeAsState(emptyList())
    val expenseListData = expenseViewModel.readAllData.observeAsState(emptyList())
    val incomeList = incomeListData.value.filter { income -> income.date.checkMonthAndYear(DateUtil.currentDateTime) }
    val expenseList = expenseListData.value.filter { expense -> expense.date.checkMonthAndYear(DateUtil.currentDateTime) }
    val calculator = remember {
        Calculator(incomeList, expenseList, context)
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
      //  RowCardCalculated(stringResource(com.emindev.expensetodolist.R.string.income),monthlyCalculator.totalIncome.toString())

    }

}


@Composable
fun RowCardCalculated(calculator: StateFlow<Calculator>) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        CardCalculated("Gelir", 1000.toString())
        CardCalculated("Gider", 500.toString())
    }
}

@Composable
fun CardCalculated(text1: String, text2: String) {

    val screenSize = DpSize(LocalConfiguration.current.screenWidthDp.dp, LocalConfiguration.current.screenHeightDp.dp)
    val size = (screenSize.width - 56.dp) / 2

    Card(modifier = Modifier
        .size(size), elevation = 16.dp, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.Green), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = text1, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Box(modifier = Modifier.height(16.dp))
            Text(text = text2, fontSize = 16.sp)
        }
    }
}