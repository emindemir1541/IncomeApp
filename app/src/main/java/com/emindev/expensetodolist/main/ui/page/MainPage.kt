package com.emindev.expensetodolist.main.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.main.common.constant.Currency
import com.emindev.expensetodolist.main.common.model.FinanceModel
import com.emindev.expensetodolist.main.data.viewmodel.FinanceViewModel
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.ui.component.ProgressBarRemainedMoney

@Composable
fun MainPage(financeViewModel: FinanceViewModel) {


    val finance = financeViewModel.finance.collectAsState(initial = FinanceModel(0f, 0f, 0f, 0f, 0f, 0f, 0f))


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, end = 16.dp, top = 32.dp), verticalArrangement = Arrangement.spacedBy(40.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            ProgressBarRemainedMoney(finance = finance, size = 100.dp)
        }
        LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                FinanceCard(title = stringResource(id = R.string.remaining_money), money = finance.value.remainedMoney.toString())
            }

            item {
                FinanceCard(title = stringResource(id = R.string.total_income), money = finance.value.totalIncome.toString())
            }

            item {
                FinanceCard(title = stringResource(id = R.string.total_expense), money = finance.value.totalExpense.toString())
            }
            item {
                FinanceCard(title = stringResource(id = R.string.paid_expense), money = finance.value.paidExpense.toString())
            }
            item {
                FinanceCard(title = stringResource(id = R.string.need), money = finance.value.totalNeed.toString())
            }
            item {
                FinanceCard(title = stringResource(id = R.string.want), money = finance.value.totalWish.toString())
            }
            item {
                FinanceCard(title = stringResource(id = R.string.debt), money = finance.value.totalDebt.toString())
            }
            item {
                Box(modifier = Modifier.height(160.dp))
            }
            item {
                Box(modifier = Modifier.height(160.dp))
            }
        }
    }
}

@Composable
private fun FinanceCard(title: String, money: String) {
    Card(modifier = Modifier) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 20.sp)
            Text(text = money + Currency.TL)

        }
    }
}