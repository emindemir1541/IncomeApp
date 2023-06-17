package com.emindev.expensetodolist.main.ui.page

import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.emindev.expensetodolist.main.common.helper.test

@Composable
@Preview
fun Test() {
    val rememberstate = remember{ mutableStateOf(false) }
    Checkbox(checked =rememberstate.value, onCheckedChange = { rememberstate.value = it  })
}