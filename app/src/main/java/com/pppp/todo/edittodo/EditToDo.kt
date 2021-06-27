package com.pppp.todo.main.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.pppp.todo.BottomSheet
import com.pppp.todo.addtodo.AddTodoViewState
import com.pppp.todo.main.ToDoViewEvent
import com.pppp.todo.main.ToDoViewEvent.OnAddToDoClicked
import com.pppp.todo.main.ToDoViewEvent.OnToDoAdded
import com.pppp.todo.main.TodoMainViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun EditBottomSheet(
    mainViewModel: TodoMainViewModel = TodoMainViewModel(),
    onEvent: (ToDoViewEvent) -> Unit = {}
) {
    BottomSheet(mainViewModel.toDoBeingEdited != null) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp
            )
        ) {
            val state = rememberSaveable { mutableStateOf(AddTodoViewState()) }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val keyboardController = LocalSoftwareKeyboardController.current

                    val onDone = {
                        keyboardController?.hide()
                        if (state.value.text.isEmpty()) {
                            state.value = state.value.copy(isError = true)
                        } else {
                            onEvent(OnAddToDoClicked)
                            onEvent(OnToDoAdded(state.value.text, state.value.dueDate))
                            state.value = state.value.copy(text = "")
                        }
                    }
                    TextField(
                        modifier = Modifier.weight(1F),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { onDone() }),
                        value = state.value.text,
                        onValueChange = {
                            state.value = state.value.copy(text = it, isError = false)
                        },
                        isError = state.value.isError
                    )
                }
            }
        }
    }
}
