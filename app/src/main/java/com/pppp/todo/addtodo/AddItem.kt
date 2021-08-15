package com.pppp.todo.main.view

import androidx.compose.foundation.layout.Row
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.pppp.uielements.ClosableInputText
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

            BottomSheet(
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
                    ClosableInputText(
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
    }
}
