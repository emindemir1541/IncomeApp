package com.emindev.expensetodolist.expense.ui.pages

import androidx.compose.runtime.Composable


import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.expense.data.room.Expense
import com.emindev.expensetodolist.expense.data.room.ExpenseEvent
import com.emindev.expensetodolist.expense.data.room.ExpenseModel
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.main.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.helper.DateUtil.Companion.convertToString
import com.emindev.expensetodolist.main.common.helper.DateUtil.Companion.isMonthAndYearBiggerThan
import com.emindev.expensetodolist.main.common.constant.Currency
import com.emindev.expensetodolist.main.common.constant.Page
import com.emindev.expensetodolist.main.common.helper.DateUtil.Companion.isMonthAndYearEqualOrSmallerThan
import com.emindev.expensetodolist.main.common.helper.test
import com.emindev.expensetodolist.main.common.util.ColorUtil
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.ui.component.AlertDialogDelete
import com.emindev.expensetodolist.main.ui.component.AnimatedVisibilityTextField
import com.emindev.expensetodolist.main.ui.component.TextSizeable
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpensePage(
    navController: NavController,
    mainViewModel: MainViewModel,
    expenseViewModel: ExpenseViewModel,
    listState: LazyListState,
    onEvent: (ExpenseEvent) -> Unit
) {
    Surface {

        val expenseState by expenseViewModel.state.collectAsState()
        val alertDialogState = rememberUseCaseState(false)
        val selectedDate by mainViewModel.selectedDate.collectAsState()


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {


            items(expenseState.expenseInfinityModels) { expense ->
                Box(
                    modifier = Modifier
                        .animateItemPlacement(animationSpec = tween(durationMillis = 600))
                ) {
                    if (selectedDate.isMonthAndYearBiggerThan(DateUtil.localDateNow))
                        RowExpenseMultipleCard(expense, selectedDate)
                }
            }


            items(expenseState.expensesMultipleCardNotCompleted) { expense ->
                AlertDialogDelete(
                    onDeleteCardClick = { onEvent(ExpenseEvent.DeleteCard(expense)) },
                    onDeleteAllClick = { onEvent(ExpenseEvent.DeleteExpense(expense)) },
                    alertDialogState
                )

                Box(
                    modifier = Modifier
                        .animateItemPlacement(animationSpec = tween(durationMillis = 600))
                ) {
                    RowExpenseMultipleCard(expense, {
                        val checkedExpense = expense.copy(completed = it)
                        onEvent(ExpenseEvent.Complete(checkedExpense))
                    }) {
                        if (expense.isCardPassed) {
                            alertDialogState.show()
                        } else {
                            expenseViewModel.setState(expense)
                            navController.navigate(Page.ExpenseUpdate.route)
                        }
                    }
                }
            }

            item {
                AnimatedVisibilityTextField(
                    visible = selectedDate.isMonthAndYearEqualOrSmallerThan(
                        DateUtil.localDateNow
                    ) && expenseState.expensesMultipleCardNotCompleted.isNotEmpty() && (expenseState.expensesMultipleCardCompleted.isNotEmpty() || expenseState.expensesOneCard.isNotEmpty())
                ) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            items(expenseState.expensesMultipleCardCompleted) { expense ->
                AlertDialogDelete(
                    onDeleteCardClick = { onEvent(ExpenseEvent.DeleteCard(expense)) },
                    onDeleteAllClick = { onEvent(ExpenseEvent.DeleteExpense(expense)) },
                    alertDialogState
                )

                Box(
                    modifier = Modifier
                        .animateItemPlacement(animationSpec = tween(durationMillis = 600))
                ) {
                    RowExpenseMultipleCard(expense, {
                        val checkedExpense = expense.copy(completed = it)
                        onEvent(ExpenseEvent.Complete(checkedExpense))
                    }) {
                        if (expense.isCardPassed) {
                            alertDialogState.show()
                        } else {
                            expenseViewModel.setState(expense)
                            navController.navigate(Page.ExpenseUpdate.route)
                        }
                    }
                }
            }




            items(expenseState.expensesOneCard) { expense ->
                Box(
                    modifier = Modifier
                        .animateItemPlacement(animationSpec = tween(durationMillis = 600))
                ) {
                    RowExpenseOneCard(expense) {
                        expenseViewModel.setState(expense)
                        navController.navigate(Page.ExpenseUpdate.route)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(100.dp))
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowExpenseMultipleCard(
    expense: Expense,
    onCheckedChanged: (Boolean) -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (expense.completed) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary,
                RoundedCornerShape(16.dp)
            )
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = expense.lender, fontSize = 20.sp)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextSizeable(text = expense.name, fontSize = 40)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), horizontalArrangement = Arrangement.End
                    ) {
                        Checkbox(
                            checked = expense.completed,
                            onCheckedChange = { onCheckedChanged(it) })
                    }
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextRemainedDay(expense = expense)
                Text(text = expense.currentAmount.toString() + Currency.TL)  // TODO: handle the cardPassed error
                Text(text = expense.currentLocalDate.convertToString(DateUtil.Delimiters.slash))
            }
            Spacer(modifier = Modifier.padding(6.dp))
        }
    }
}

@Composable
fun RowExpenseMultipleCard(expense: ExpenseModel, selectedDate: LocalDate) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth(),

        elevation = CardDefaults.cardElevation(0.dp),
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextSizeable(text = expense.name, fontSize = 40)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val date = DateUtil.convertToDate(
                    selectedDate.year,
                    selectedDate.monthValue,
                    expense.initialLocalDate.dayOfMonth
                )
                TextRemainedDay(date)
                Text(text = expense.latestAmount.toString() + Currency.TL)  // TODO: handle the cardPassed error
                Text(text = date.convertToString(DateUtil.Delimiters.slash))
            }
            Spacer(modifier = Modifier.padding(6.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RowExpenseOneCard(expense: Expense, onLongClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextSizeable(text = expense.name, fontSize = 25)
                Text(text = expense.currentLocalDate.convertToString(DateUtil.Delimiters.slash))
                Text(text = if (expense.isCardPassed) expense.currentAmount.toString() else expense.latestAmount.toString() + Currency.TL)  // TODO: handle the cardPassed error

            }

        }
    }
}

@Composable
private fun TextRemainedDay(expense: Expense) {

    if (expense.completed) {
        Text(
            text = stringResource(id = R.string.paid),
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Bold
        )
    } else
        if (expense.remainedDay > 0L) {

            Text(
                text = (expense.remainedDay.toString() + " " + stringResource(R.string.day_remained)),
                color = if (expense.remainedDayAsPercentage > 50) Color.Unspecified else ColorUtil.getColorBetweenRedAndYellow(
                    expense.remainedDayAsPercentage * 2
                ), fontWeight = FontWeight.Bold
            )
        } else if (expense.remainedDay < 0L) {
            Text(
                text = (expense.remainedDay.absoluteValue.toString() + " " + stringResource(R.string.day_passed)),
                color = Color.Red, fontWeight = FontWeight.Bold
            )
        } else
            Text(
                text = stringResource(id = R.string.payment_time),
                color = Color.Red
            )
}

@Composable
private fun TextRemainedDay(selectedDate: LocalDate) {
    Text(
        text = (DateUtil.dayBetweenTwoDate(
            selectedDate,
            DateUtil.localDateNow
        )).toString() + " " + stringResource(R.string.day_remained),
        fontWeight = FontWeight.Bold
    )
}



