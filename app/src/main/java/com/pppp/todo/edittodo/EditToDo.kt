package com.pppp.todo.edittodo

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.R
import com.pppp.todo.edittodo.EditTodoViewEvent.Init
import com.pppp.todo.edittodo.EditTodoViewEvent.OnBackPressed
import com.pppp.todo.edittodo.EditTodoViewEvent.OnDateTimePicked
import com.pppp.todo.edittodo.EditTodoViewEvent.OnDoneClicked
import com.pppp.todo.edittodo.EditTodoViewEvent.OnTextChanged
import com.pppp.todo.exaustive
import com.pppp.todo.main.MainActivityViewModel.ViewState.EditItem
import com.pppp.todo.main.MainActivityViewModel.ViewState.EditItem.None
import com.pppp.todo.main.MainActivityViewModel.ViewState.EditItem.Some
import com.pppp.todo.main.viewmodel.MainViewEvent.OnCancel
import com.pppp.uielements.BottomSheet
import com.pppp.uielements.Calendar
import com.pppp.uielements.ClosableInputText
import kotlinx.coroutines.flow.collect

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun EditItem(
    itemBeingEdited: EditItem = None,
    editViewModel: EditViewModel = viewModel(),
    onCancel: () -> Unit = {}
) {
    val state by editViewModel.states.collectAsState()

    LaunchedEffect(editViewModel, onCancel) {
        editViewModel.oneOffEvents.collect {
            when (it) {
                is OneOffEvents.OnCancel -> onCancel()
            }.exaustive
        }
    }
    LaunchedEffect(itemBeingEdited) {
        if (itemBeingEdited is Some) {
            editViewModel(
                Init(
                    itemId = itemBeingEdited.itemId,
                    listId = itemBeingEdited.listId
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
                text = state.title,
                label = stringResource(R.string.edit_todo),
                isError = state.isError
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
