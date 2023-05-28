@file:OptIn(ExperimentalMaterial3Api::class)

package com.emindev.expensetodolist.main.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.isMonthAndYearEqualTo
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.monthString
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@Composable
fun DateRow(mainViewModel: MainViewModel) {
    val selectedDate = mainViewModel.selectedDate.collectAsState()
    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
        IconButton(onClick = { mainViewModel.previousMonth }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
        }
        Button(onClick = {
            if (selectedDate.value.isMonthAndYearEqualTo(DateUtil.localDateNow))
                calendarState.show()
            else
                mainViewModel.setSelectedDate(DateUtil.localDateNow)
        }) {
            Text(text = selectedDate.value.monthString())
        }
        IconButton(onClick = { mainViewModel.nextMonth }) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = stringResource(id = R.string.forward))
        }
    }
    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(yearSelection = true, monthSelection = true),
        selection = CalendarSelection.Date(selectedDate = selectedDate.value) { newDate ->
        mainViewModel.setSelectedDate(newDate)
    }

    )
}