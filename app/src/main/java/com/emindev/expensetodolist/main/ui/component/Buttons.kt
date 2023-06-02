package com.emindev.expensetodolist.main.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.emindev.expensetodolist.R

@Composable
fun ButtonAdd(onClick:()->Unit) {
    Button(modifier = Modifier.fillMaxWidth(),onClick = onClick) {
        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.add))
    }
}

@Composable
 fun HideAbleButtonContent(
    isVisibleBecauseOfScrolling: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisibleBecauseOfScrolling,
        enter =  slideInVertically()+ fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        content()

    }
}