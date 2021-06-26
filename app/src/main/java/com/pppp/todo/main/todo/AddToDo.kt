package com.pppp.todo.main.todo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.datetimepicker

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AddToDo(
    onToDoAdded: (String) -> Unit = {}
) {
    Column(modifier = Modifier.padding(start = 16.dp,end = 16.dp, top = 16.dp, bottom = 8.dp)) {
        AddToDoInputControl(onToDoAdded)
    }
}

@ExperimentalComposeUiApi
@Composable
private fun AddToDoInputControl(onToDoAdded: (String) -> Unit = {}) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var isError by rememberSaveable { mutableStateOf(false) }
            var textState by rememberSaveable { mutableStateOf("") }
            val keyboardController = LocalSoftwareKeyboardController.current

            val onDone = {
                keyboardController?.hide()
                if (textState.isEmpty()) {
                    isError = true
                } else {
                    onToDoAdded(textState)
                    textState = ""
                }
            }
            var isError1 = isError
            TextField(
                modifier = Modifier.weight(1F),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onDone() }),
                value = textState,
                onValueChange = {
                    textState = it
                    isError1 = false
                },
                isError = isError1
            )
            DoneButton(onDone)
        }
        Row {
            Calendar()
        }
    }
}

@Composable
private fun Calendar() {
    val dialog = remember { MaterialDialog() }
    dialog.build {
        datetimepicker()
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                dialog.show()
            }
            .padding(all = 8.dp)
    ) {
        Image(
            imageVector = Icons.Outlined.DateRange,
            contentDescription = "",
        )
    }
}

@Composable
private fun DoneButton(onDone: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(8.dp, 0.dp, 0.dp, 0.dp)
            .size(32.dp),
        shape = CircleShape,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
    ) {
        Image(
            imageVector = Icons.Filled.ArrowUpward,
            contentDescription = "",
            modifier = Modifier.clickable {
                onDone()
            })
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Preview
@Composable
fun previewAddToDo() {
    AddToDo()
}

