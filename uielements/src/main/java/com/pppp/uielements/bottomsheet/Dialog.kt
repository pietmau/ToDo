package com.pppp.uielements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun DialogControls(
    onConfirmClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    optionalControls: @Composable () -> Unit = { }
) {
    optionalControls()
    Buttons(onCancelClicked, onConfirmClicked)
}

@Composable
private fun Buttons(
    onCancelClicked: () -> Unit = {},
    onConfirmClicked: () -> Unit = {},
    cancel: String = "Cancel",
    save: String = "Save",
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            modifier = Modifier.padding(end = 8.dp, top = 8.dp),
            onClick = onCancelClicked
        ) {
            Text(text = cancel)
        }
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = onConfirmClicked
        ) {
            Text(text = save)
        }
    }
}
