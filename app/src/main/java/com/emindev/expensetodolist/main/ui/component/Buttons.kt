package com.emindev.expensetodolist.main.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emindev.expensetodolist.R

@Composable
fun ButtonAdd(onClick:()->Unit) {
    Button(modifier = Modifier.fillMaxWidth(),onClick = onClick) {
        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.add))
    }
}