package com.pppp.uielements

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction

@ExperimentalComposeUiApi
@Composable
fun ClosableInputText(
    onBackPressed: () -> Unit = {},
    onDoneClicked: () -> Unit = {},
    onTitleChanged: (String) -> Unit = { _: String -> },
    text: String = "",
    isError: Boolean = false,
    label: String? = null,
    placeHolder: String? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            modifier = Modifier
                .weight(1F)
                .onKeyEvent {
                    if (it.key.keyCode == Key.Back.keyCode) {
                        onBackPressed()
                    }
                    false
                },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onDoneClicked() }),
            value = text,
            onValueChange = onTitleChanged,
            isError = isError,
            label = { label?.let { Text(it) } },
            placeholder = { placeHolder?.let { Text(it) } },
        )
    }
}

