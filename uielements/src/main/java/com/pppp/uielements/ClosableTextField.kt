package com.pppp.uielements

import android.util.Log
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@ExperimentalComposeUiApi
@Composable
fun ClosableErrorTextField(
    modifier: Modifier,
    keyboardOptions: KeyboardOptions,
    onDone: KeyboardActionScope.() -> Unit = {},
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf(value) }
    var isError by remember { mutableStateOf(isError) }

    TextField(
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = {
            if (text.isEmpty()) {
                isError = true
                return@KeyboardActions
            } else {
                keyboardController?.hide()
                onDone()
            }
        }),
        value = value,
        onValueChange = {
            text = it
            onValueChange(text)
        },
        isError = isError
    )
}
