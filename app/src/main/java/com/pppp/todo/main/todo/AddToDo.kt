package com.pppp.todo.main.todo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.ModalBottomSheetValue.Expanded
import androidx.compose.runtime.saveable.rememberSaveable
import com.pppp.entities.ToDo

@ExperimentalMaterialApi
@Composable
fun AddToDo(
    onToDoAdded: (String) -> Unit = {}
) {
    Column(modifier = Modifier.padding(16.dp)) {
        AddToDoInputControl(onToDoAdded)
    }
}

@Composable
private fun AddToDoInputControl(onToDoAdded: (String) -> Unit = {}) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        var isError by rememberSaveable { mutableStateOf(false) }
        var textState by rememberSaveable { mutableStateOf("") }
        val onDone: () -> Unit = {
            if (textState.isEmpty()) {
                isError = true
            } else {
                onToDoAdded(textState)
                textState = ""
            }
        }
        TextField(
            modifier = Modifier.weight(1F),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            value = textState,
            onValueChange = {
                textState = it
                isError = false
            },
            isError = isError
        )
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
}

@ExperimentalMaterialApi
@Preview
@Composable
fun previewAddToDo() {
    AddToDo()
}

