package com.emindev.expensetodolist.main.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.emindev.expensetodolist.helperlibrary.common.helper.test
import com.emindev.expensetodolist.main.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.common.constant.CardSettings

@Composable
fun MainPage(
) {
    val context = LocalContext.current

    val totalIncome = CardSettings(0, true, "Income", 120)
    val totalExpense = CardSettings(1, true, "Expense", 100)

    val listOfCalculations = mutableListOf<CardSettings>(totalExpense, totalIncome)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        content = {
            items(listOfCalculations) { item: CardSettings ->
                CardCalculated(cardSettings = item) {
                    listOfCalculations.add(totalIncome)
                    context.test(listOfCalculations)
                }
            }
        }
    )

}


@Composable
fun CardCalculated(cardSettings: CardSettings, clickable: () -> Unit) {
    val context = LocalContext.current

    if (cardSettings.isOpen)
        Card(modifier = Modifier.padding(16.dp), elevation = 16.dp, shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier
                .size(150.dp)
                .background(Color.Green)
                .clickable { clickable() }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = cardSettings.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.padding(16.dp))
                Text(text = cardSettings.calculatedValue.toString(), fontSize = 16.sp)
            }
        }
}

