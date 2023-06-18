package com.emindev.expensetodolist.main.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emindev.expensetodolist.main.common.helper.addLog
import com.emindev.expensetodolist.main.common.model.FinanceModel
import java.lang.Exception

@Composable
fun ProgressBarRemainedMoney(finance: State<FinanceModel>, modifier: Modifier = Modifier, size: Dp = 40.dp) {

    val animatedProgress = remember { Animatable(0.00000000001f) }

    LaunchedEffect(finance.value) {
        val financeBase = finance.value
        val percentage = if (financeBase.totalIncome != 0f)
            ((financeBase.remainedMoney * 100) / financeBase.totalIncome)
        else
            0f
        animatedProgress.animateTo(
            targetValue = percentage,
            animationSpec = tween(durationMillis = 500)
        )
    }

    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(progress = animatedProgress.value / 100, modifier.fillMaxSize())
        Text(text = finance.value.remainedMoney.toString())
    }
}

