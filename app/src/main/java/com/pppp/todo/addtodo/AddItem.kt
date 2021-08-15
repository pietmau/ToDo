package com.pppp.todo.main.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.addtodo.AddTodoViewModel
import com.pppp.todo.addtodo.Event.DoneClicked
import com.pppp.todo.addtodo.Event.OnBackPressed
import com.pppp.todo.addtodo.Event.OnTimeDataPicked
import com.pppp.todo.addtodo.Event.OnTitleChanged
import com.pppp.todo.addtodo.OneOffEvent.AddToDo
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnCancel
import com.pppp.todo.main.viewmodel.MainViewEvent.OnToDoAdded
import com.pppp.uielements.BottomSheet
import com.pppp.uielements.Calendar
import kotlinx.coroutines.flow.collect
import com.pppp.todo.addtodo.OneOffEvent.OnBackPressed as OnBackClicked

interface AddItem {

    companion object {

        @ExperimentalComposeUiApi
        @ExperimentalMaterialApi
        @Composable
        fun Content(
            isVisible: Boolean = false,
            onEvent: (MainViewEvent) -> Unit = {},
        ) {
            val viewmodel: AddTodoViewModel = viewModel()
            val viewState by viewmodel.states.collectAsState()

            LaunchedEffect(onEvent) {
                viewmodel.oneOffEvents.collect {
                    when (it) {
                        is AddToDo -> onEvent(
                            OnToDoAdded(
                                title = it.title,
                                due = it.due
                            )
                        )
                        is OnBackClicked -> onEvent(OnCancel)
                    }.exaustive
                }
            }

            BottomSheet.Content(
                isExpanded = isVisible,
                onDismissed = {
                    viewmodel(OnBackPressed)
                },
                onConfirmClicked = {
                    viewmodel(DoneClicked)
                },
                onCancelClicked = {
                    viewmodel(OnBackPressed)
                },
                sheetContent = {
                    InputTextRow(
                        onBackPressed = { viewmodel(OnBackPressed) },
                        onDoneClicked = { viewmodel(DoneClicked) },
                        onTitleChanged = { viewmodel(OnTitleChanged(it)) },
                        title = viewState.title,
                        isError = viewState.isError
                    )
                },
                optionalDialogControls = {
                    Row {
                        Calendar.Content(
                            due = viewState.due,
                            onTimeDataPicked = { viewmodel(OnTimeDataPicked(it)) }
                        )
                    }
                }
            )
        }

        @ExperimentalComposeUiApi
        @Composable
        private fun InputTextRow(
            onBackPressed: () -> Unit = {},
            onDoneClicked: () -> Unit = {},
            onTitleChanged: (String) -> Unit = { _: String -> },
            title: String = "",
            isError: Boolean = false
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
                    value = title,
                    onValueChange = onTitleChanged,
                    isError = isError
                )
            }
        }
    }
}
