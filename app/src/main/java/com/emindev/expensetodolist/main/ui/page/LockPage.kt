package com.emindev.expensetodolist.main.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emindev.expensetodolist.main.ui.component.LocalColor

@Composable
@Preview
fun LockPage() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(LocalColor.lockScreenBackColor), contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.spacedBy(64.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Default.Lock, contentDescription = "lock icon", modifier = Modifier.size(80.dp), tint = Color.White)
            Text(text = "This application has locked by the admin", fontSize = 20.sp, color = Color.White)
        }
    }
    BackHandler() {}

}