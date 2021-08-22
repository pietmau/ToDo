package com.pppp.todo.edittodo

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.edittodo.EditTodoViewEvent.Init
import com.pppp.todo.edittodo.EditTodoViewEvent.OnBackPressed
import com.pppp.todo.edittodo.EditTodoViewEvent.OnDateTimePicked
import com.pppp.todo.edittodo.EditTodoViewEvent.OnDoneClicked
import com.pppp.todo.edittodo.EditTodoViewEvent.OnTextChanged
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.ItemBeingEdited
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnCancel
import com.pppp.uielements.BottomSheet
import com.pppp.uielements.Calendar
import com.pppp.uielements.ClosableInputText
import kotlinx.coroutines.flow.collect

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun EditItem(
    item: ItemBeingEdited = ItemBeingEdited.None,
    editViewModel: EditViewModel = viewModel(),
    onEvent: (MainViewEvent) -> Unit = {}
) {
    val state by editViewModel.states.collectAsState()
    LaunchedEffect(editViewModel, onEvent) {
        editViewModel.oneOffEvents.collect {
            when (it) {
                is OneOffEvents.OnCancel -> onEvent(OnCancel)
            }.exaustive
        }
    }
    LaunchedEffect(item) {
        if (item is ItemBeingEdited.Some) {
            editViewModel(
                Init(
                    itemId = item.itemId,
                    listId = item.listId
                )
            )
        }
    }

    BottomSheet(
        isExpanded = state.isVisible,
        onDismissed = {
            editViewModel(OnBackPressed)
        },
        onConfirmClicked = {
            editViewModel(OnDoneClicked)
        },
        onCancelClicked = {
            editViewModel(OnBackPressed)
        },
        sheetContent = {
            ClosableInputText(
                onBackPressed = { editViewModel(OnBackPressed) },
                onDoneClicked = { editViewModel(OnDoneClicked) },
                onTitleChanged = { editViewModel(OnTextChanged(it)) },
                text = state.title
            )
        },
        optionalDialogControls = {
                Calendar(
                    due = state.due,
                    onTimeDataPicked = { editViewModel(OnDateTimePicked(it)) }
                )
        },
    )
}
