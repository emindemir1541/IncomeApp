package com.emindev.expensetodolist.main.ui.component

import android.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
fun AlertDialogDelete(onDeleteCardClick: () -> Unit, onDeleteAllClick: () -> Unit, useCaseState: UseCaseState) {

    CoreDialog(
        state = useCaseState,
        selection = CoreSelection(
            withButtonView = false,
        ),
        onPositiveValid = true,
        body = {
            Column(modifier = Modifier.padding(36.dp), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(text = stringResource(id = R.string.info_delete_card))
                }
                Spacer(modifier = Modifier.size(30.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    FilledTonalButton(onClick = { onDeleteCardClick(); useCaseState.hide() }) {
                        Text(text = stringResource(id = R.string.delete_this_card))
                    }
                    Button(onClick = { onDeleteAllClick();useCaseState.hide()}) {
                        Text(text = stringResource(id = R.string.delete_income))
                    }

                }
            }

        },
    )


}