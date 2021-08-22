package com.pppp.todo.addtodo

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
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

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun AddItem(
    isVisible: Boolean = false,
    onEvent: (MainViewEvent) -> Unit = {},
) {
    val viewModel: AddTodoViewModel = viewModel()
    val viewState by viewModel.states.collectAsState()

    LaunchedEffect(onEvent) {
        viewModel.oneOffEvents.collect {
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
            viewModel(OnBackPressed)
        },
        onConfirmClicked = {
            viewModel(DoneClicked)
        },
        onCancelClicked = {
            viewModel(OnBackPressed)
        },
        sheetContent = {
            ClosableInputText(
                onBackPressed = { viewModel(OnBackPressed) },
                onDoneClicked = { viewModel(DoneClicked) },
                onTitleChanged = { viewModel(OnTitleChanged(it)) },
                text = viewState.title,
                isError = viewState.isError
            )
        },
        optionalDialogControls = {
            Calendar(
                due = viewState.due,
                onTimeDataPicked = { viewModel(OnTimeDataPicked(it)) }
            )
        }
    )
}
