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

interface ConfirmOrCancel {

    companion object {

        @Composable
        fun DialogControls(
            due: Long?,
            onTimeDataPicked: (Long?) -> Unit,
            onConfirmClicked: () -> Unit,
            onCancelClicked: () -> Unit
        ) {
            Row {
                Calendar.Content(due, onTimeDataPicked)
            }
            Buttons(onCancelClicked, onConfirmClicked)
        }

        @Composable
        fun Buttons(
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
    }
}