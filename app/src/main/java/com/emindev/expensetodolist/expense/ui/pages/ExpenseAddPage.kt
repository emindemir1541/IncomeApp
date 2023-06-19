package com.emindev.expensetodolist.expense.ui.pages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.expense.data.room.ExpenseEvent
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.ui.component.AnimatedVisibilityTextField
import com.emindev.expensetodolist.main.ui.component.FilterChipColored
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAddPage(navController: NavController, mainViewModel: MainViewModel, expenseViewModel: ExpenseViewModel, onEvent: (ExpenseEvent) -> Unit) {

    val context = LocalContext.current
    onEvent(ExpenseEvent.ShowDialog)
    val calendarState = rememberUseCaseState()

    val expenseState by expenseViewModel.state.collectAsState()

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(yearSelection = false, monthSelection = false),
        selection = CalendarSelection.Date(selectedDate = expenseState.currentDate) { newDate ->
            val date = LocalDate.of((expenseState.initialDate).year, (expenseState.initialDate).monthValue, newDate.dayOfMonth)
            onEvent(ExpenseEvent.SetDate(date))
        }

    )

    Dialog(onDismissRequest = {
        navController.popBackStack()
        expenseViewModel.clearState()
    }, properties = DialogProperties(usePlatformDefaultWidth = false)) {


        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            item {

                TopAppBar(title = { Text(text = stringResource(id = R.string.expense_add)) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
            }
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = expenseState.name, onValueChange = { onEvent(ExpenseEvent.SetName(it)) }, label = { Text(stringResource(id = R.string.expense_name)) })

            }
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = expenseState.latestAmount, onValueChange = { onEvent(ExpenseEvent.SetAmount(it)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), label = { Text(stringResource(id = R.string.amount)) })
            }
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    FilterChipColored(label = { Text(text = stringResource(id = R.string.debt)) }, selected = expenseState.expenseType == ExpenseType.DEBT, onClick = { onEvent(ExpenseEvent.SetExpenseType(ExpenseType.DEBT)) })
                    FilterChipColored(onClick = { onEvent(ExpenseEvent.SetExpenseType(ExpenseType.NEED)) }, label = { Text(text = stringResource(id = R.string.need)) }, selected = expenseState.expenseType == ExpenseType.NEED)
                    FilterChipColored(onClick = { onEvent(ExpenseEvent.SetExpenseType(ExpenseType.UNNECESSARY)) }, label = { Text(text = stringResource(id = R.string.want)) }, selected = expenseState.expenseType == ExpenseType.UNNECESSARY)
                }
            }

            item {
                AnimatedVisibilityTextField(visible = expenseState.expenseType == ExpenseType.DEBT) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = expenseState.lender, onValueChange = { onEvent(ExpenseEvent.SetLenderName(it)) }, label = { Text(text = stringResource(id = R.string.lender)) })
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    FilterChipColored(label = { Text(text = stringResource(id = R.string.expense_forOnce)) }, selected = expenseState.repeatType == RepeatType.ONCE, onClick = { onEvent(ExpenseEvent.SetRepeatType(RepeatType.ONCE)) })
                    FilterChipColored(onClick = { onEvent(ExpenseEvent.SetRepeatType(RepeatType.LIMITED)) }, label = { Text(text = stringResource(id = R.string.limited)) }, selected = expenseState.repeatType == RepeatType.LIMITED)
                    FilterChipColored(onClick = { onEvent(ExpenseEvent.SetRepeatType(RepeatType.INFINITY)) }, label = { Text(text = stringResource(id = R.string.every_month)) }, selected = expenseState.repeatType == RepeatType.INFINITY)
                }
            }

            item {
                AnimatedVisibilityTextField(visible = expenseState.repeatType == RepeatType.LIMITED) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = expenseState.repetition, onValueChange = { onEvent(ExpenseEvent.SetRepetition(it)) }, label = { Text(text = stringResource(id = R.string.Repetition)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    }
                }
            }
            item {
                OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { mainViewModel.interactionFunction(2000L) { calendarState.show() } }) {
                    Text(text = stringResource(id = R.string.expense_date))
                }
            }
            item {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    if (expenseViewModel.stateSaveValid) {
                        Toast.makeText(context, context.getString(R.string.warning_empty), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        mainViewModel.interactionFunction(3000L) {
                            navController.popBackStack()
                            onEvent(ExpenseEvent.SaveExpense)
                            expenseViewModel.clearState()
                        }
                    }
                }) {
                    Text(text = stringResource(id = R.string.add))
                }
            }
        }


    }

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseUpdatePage(navController: NavController, mainViewModel: MainViewModel, expenseViewModel: ExpenseViewModel, onEvent: (ExpenseEvent) -> Unit) {

    val context = LocalContext.current
    onEvent(ExpenseEvent.ShowDialog)
    val calendarState = rememberUseCaseState()

    val expenseState by expenseViewModel.state.collectAsState()

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(yearSelection = false, monthSelection = false),
        selection = CalendarSelection.Date(selectedDate = expenseState.currentDate) { newDate ->
            val date = LocalDate.of((expenseState.initialDate).year, (expenseState.initialDate).monthValue, newDate.dayOfMonth)
            onEvent(ExpenseEvent.SetDate(date))
        }

    )

    Dialog(onDismissRequest = {
        navController.popBackStack()
        expenseViewModel.clearState()
    }, properties = DialogProperties(usePlatformDefaultWidth = false)) {


        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            item {

                TopAppBar(title = { Text(text = stringResource(id = R.string.update_expense)) }, actions = {
                    IconButton(onClick = {
                        mainViewModel.interactionFunction(3000L) {
                            onEvent(ExpenseEvent.DeleteExpense(expenseState.toExpense()))
                            onEvent(ExpenseEvent.HideDialog)
                            expenseViewModel.clearState()
                            navController.popBackStack()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete), tint = Color.Red)
                    }
                })
            }
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = expenseState.name, onValueChange = { onEvent(ExpenseEvent.SetName(it)) }, label = { Text(stringResource(id = R.string.expense_name)) })

            }
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = expenseState.latestAmount, onValueChange = { onEvent(ExpenseEvent.SetAmount(it)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), label = { Text(stringResource(id = R.string.amount)) })
            }
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    FilterChipColored(label = { Text(text = stringResource(id = R.string.debt)) }, selected = expenseState.expenseType == ExpenseType.DEBT, onClick = { onEvent(ExpenseEvent.SetExpenseType(ExpenseType.DEBT)) })
                    FilterChipColored(onClick = { onEvent(ExpenseEvent.SetExpenseType(ExpenseType.NEED)) }, label = { Text(text = stringResource(id = R.string.need)) }, selected = expenseState.expenseType == ExpenseType.NEED)
                    FilterChipColored(onClick = { onEvent(ExpenseEvent.SetExpenseType(ExpenseType.UNNECESSARY)) }, label = { Text(text = stringResource(id = R.string.want)) }, selected = expenseState.expenseType == ExpenseType.UNNECESSARY)
                }
            }

            item {
                AnimatedVisibilityTextField(visible = expenseState.expenseType == ExpenseType.DEBT) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = expenseState.lender, onValueChange = { onEvent(ExpenseEvent.SetLenderName(it)) }, label = { Text(text = stringResource(id = R.string.lender)) })
                    }
                }
            }

            item {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    if (expenseViewModel.stateUpdateValid) {
                        Toast.makeText(context, context.getString(R.string.warning_empty), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        mainViewModel.interactionFunction(3000L) {
                            navController.popBackStack()
                            onEvent(ExpenseEvent.UpdateExpense)
                            expenseViewModel.clearState()
                        }
                    }


                }) {
                    Text(text = stringResource(id = R.string.update))
                }
            }
        }


    }

}
