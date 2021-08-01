package com.pppp.todo.main.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.addtodo.AddTodoViewModel
import com.pppp.todo.addtodo.Event
import com.pppp.todo.addtodo.Event.DoneClicked
import com.pppp.todo.addtodo.Event.OnBackPressed
import com.pppp.todo.addtodo.Event.OnTimeDataPicked
import com.pppp.todo.addtodo.Event.OnTitleChanged
import com.pppp.todo.addtodo.OneOffEvent.AddToDo
import com.pppp.todo.addtodo.ViewState
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnCancel
import com.pppp.todo.main.viewmodel.MainViewEvent.OnToDoAdded
import com.pppp.uielements.BottomSheet
import com.pppp.uielements.ConfirmOrCancel.Companion.DialogControls
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
                content = {
                    AddToDo(viewmodel.states.collectAsState()) {
                        viewmodel(it)
                    }
                })
        }

        @ExperimentalComposeUiApi
        @ExperimentalMaterialApi
        @Composable
        private fun AddToDo(
            viewState: State<ViewState> = remember {
                mutableStateOf(ViewState())
            },
            onEvent: (Event) -> Unit = {}
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                )
            ) {
                AddToDoInputControl(viewState, onEvent)
            }
        }

        @ExperimentalComposeUiApi
        @Composable
        private fun AddToDoInputControl(
            state: State<ViewState> = remember {
                mutableStateOf(ViewState())
            },
            onEvent: (Event) -> Unit = {}
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        modifier = Modifier
                            .weight(1F)
                            .onKeyEvent {
                                if (it.key.keyCode == Key.Back.keyCode) {
                                    onEvent(OnBackPressed)
                                }
                                false
                            },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { onEvent(DoneClicked) }),
                        value = state.value.title,
                        onValueChange = {
                            onEvent(OnTitleChanged(it))
                        },
                        isError = state.value.isError
                    )
                }
                DialogControls(
                    due = state.value.due,
                    onTimeDataPicked = {
                        onEvent(OnTimeDataPicked(it))
                    },
                    onConfirmClicked = {
                        onEvent(DoneClicked)
                    },
                    onCancelClicked = {
                        onEvent(OnBackPressed)
                    }
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Preview
@Composable
fun previewAddToDo() {
    AddItem.Content()
}