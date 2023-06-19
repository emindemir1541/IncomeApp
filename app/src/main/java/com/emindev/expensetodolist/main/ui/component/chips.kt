package com.emindev.expensetodolist.main.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipBorder
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.emindev.expensetodolist.main.ui.theme.MainTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipColored(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = FilterChipDefaults.shape,
    colors: SelectableChipColors = FilterChipDefaults.elevatedFilterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary, selectedLabelColor = MaterialTheme.colorScheme.onPrimary),
    elevation: SelectableChipElevation? = FilterChipDefaults.elevatedFilterChipElevation(),
    border: SelectableChipBorder? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }


) {
    ElevatedFilterChip(
        selected = selected,
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        elevation = elevation,
        colors = colors,
        shape = shape,
        border = border,
        interactionSource = interactionSource
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun deneme() {
    MainTheme() {

            FilterChipColored(label = { Text(text = "sldfg") }, selected = true, onClick = { })
    }
}