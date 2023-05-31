package com.emindev.expensetodolist.main.ui.component

import android.app.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.emindev.expensetodolist.R
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.ButtonStyle
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogDelete(onDeleteClick: () -> Unit, onDeleteAllClick: () -> Unit,useCaseState: UseCaseState) {

    CoreDialog(
        state = useCaseState,
        selection = CoreSelection(
            withButtonView = true,
            negativeButton = SelectionButton(
                stringResource(id = R.string.delete_this_card),
                IconSource(Icons.Rounded.Delete),
                ButtonStyle.ELEVATED
            ),
            positiveButton = SelectionButton(
                stringResource(id = R.string.delete_income),
                IconSource(Icons.Rounded.Delete),
                ButtonStyle.FILLED
            ),
        ),
        onPositiveValid = true,
        body = {
            Text(text = stringResource(id = R.string.info_delete_card))
        },
    )


}