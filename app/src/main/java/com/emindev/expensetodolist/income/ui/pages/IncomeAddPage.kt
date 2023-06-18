@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.emindev.expensetodolist.income.ui.pages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.income.data.room.IncomeEvent
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.delay
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeAddPage(navController: NavController, mainViewModel: MainViewModel, incomeViewModel: IncomeViewModel, onEvent: (IncomeEvent) -> Unit) {

    val context = LocalContext.current
    onEvent(IncomeEvent.ShowDialog)
    val calendarState = rememberUseCaseState()

    val incomeState by incomeViewModel.state.collectAsState()


    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(yearSelection = false, monthSelection = false),
        selection = CalendarSelection.Date(selectedDate = incomeState.currentDate) { newDate ->
            val date = LocalDate.of((incomeState.initialDate).year, (incomeState.initialDate).monthValue, newDate.dayOfMonth)
            onEvent(IncomeEvent.SetDate(date))
        }

    )

    Dialog(onDismissRequest = {
        navController.popBackStack()
        incomeViewModel.clearState()
    }, properties = DialogProperties(usePlatformDefaultWidth = false)) {


        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            item {

                TopAppBar(title = { Text(text = stringResource(id = R.string.income_add)) })
            }
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = incomeState.name, onValueChange = { onEvent(IncomeEvent.SetName(it)) }, label = { Text(stringResource(id = R.string.income_name)) })

            }
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = incomeState.latestAmount, onValueChange = { onEvent(IncomeEvent.SetAmount(it)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), label = { Text(stringResource(id = R.string.amount)) })
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    ElevatedFilterChip(label = { Text(text = stringResource(id = R.string.income_forOnce)) }, selected = incomeState.repeatType == RepeatType.ONCE, onClick = { onEvent(IncomeEvent.SetRepeatType(RepeatType.ONCE)) })
                    ElevatedFilterChip(onClick = { onEvent(IncomeEvent.SetRepeatType(RepeatType.LIMITED)) }, label = { Text(text = stringResource(id = R.string.limited)) }, selected = incomeState.repeatType == RepeatType.LIMITED)
                    ElevatedFilterChip(onClick = { onEvent(IncomeEvent.SetRepeatType(RepeatType.INFINITY)) }, label = { Text(text = stringResource(id = R.string.every_month)) }, selected = incomeState.repeatType == RepeatType.INFINITY)
                }
            }

            item {
                AnimatedVisibility(visible = incomeState.repeatType == RepeatType.LIMITED) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = incomeState.repetition, onValueChange = { onEvent(IncomeEvent.SetRepetition(it)) }, label = { Text(text = stringResource(id = R.string.Repetition)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    }
                }
            }
            item {
                OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { mainViewModel.interactionFunction(2000L) { calendarState.show() } }) {
                    Text(text = stringResource(id = R.string.income_date))
                }
            }
            item {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {

                    if (!incomeViewModel.stateSaveValid) {
                        Toast.makeText(context, context.getString(R.string.warning_empty), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        mainViewModel.interactionFunction(3000L) {
                            navController.popBackStack()
                            onEvent(IncomeEvent.SaveIncome)
                            incomeViewModel.clearState()
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
fun IncomeUpdatePage(navController: NavController, mainViewModel: MainViewModel, incomeViewModel: IncomeViewModel, onEvent: (IncomeEvent) -> Unit) {

    val context = LocalContext.current
    onEvent(IncomeEvent.ShowDialog)
    val calendarState = rememberUseCaseState()

    val incomeState by incomeViewModel.state.collectAsState()

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(yearSelection = false, monthSelection = false),
        selection = CalendarSelection.Date(selectedDate = incomeState.currentDate) { newDate ->
            val date = LocalDate.of((incomeState.initialDate).year, (incomeState.initialDate).monthValue, newDate.dayOfMonth)
            onEvent(IncomeEvent.SetDate(date))
        }

    )

    Dialog(onDismissRequest = {
        navController.popBackStack()
        incomeViewModel.clearState()
    }, properties = DialogProperties(usePlatformDefaultWidth = false)) {


        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            item {

                TopAppBar(title = { Text(text = stringResource(id = R.string.income_update)) }, actions = {
                    IconButton(onClick = {
                        mainViewModel.interactionFunction(3000L) {
                            onEvent(IncomeEvent.DeleteIncome(incomeState.toIncome()));
                            onEvent(IncomeEvent.HideDialog)
                            incomeViewModel.clearState()
                            navController.popBackStack()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete), tint = Color.Red)
                    }
                })
            }
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = incomeState.name, onValueChange = { onEvent(IncomeEvent.SetName(it)) }, label = { Text(stringResource(id = R.string.income_name)) })

            }
            item {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = incomeState.latestAmount, onValueChange = { onEvent(IncomeEvent.SetAmount(it)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), label = { Text(stringResource(id = R.string.amount)) })
            }

            item {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    if (!incomeViewModel.stateUpdateValid) {
                        Toast.makeText(context, context.getString(R.string.warning_empty), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        mainViewModel.interactionFunction(3000L) {

                            navController.popBackStack()
                            onEvent(IncomeEvent.UpdateIncome)
                            incomeViewModel.clearState()
                        }
                    }
                }) {
                    Text(text = stringResource(id = R.string.update))
                }
            }
        }


    }

}