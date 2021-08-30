package com.pppp.todo.addtodo

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.R
import com.pppp.todo.addtodo.Event.Init
import com.pppp.todo.addtodo.Event.DoneClicked
import com.pppp.todo.addtodo.Event.OnBackPressed
import com.pppp.todo.addtodo.Event.OnTimeDataPicked
import com.pppp.todo.addtodo.Event.OnTitleChanged
import com.pppp.todo.addtodo.OneOffEvent.AddToDo
import com.pppp.todo.exaustive
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
    listId: String? = null,
    onToDoAdded: (listId: String, title: String, due: Long?) -> Unit = { _, _, _ -> },
    onBackPressed: () -> Unit = {}
) {
    listId ?: return
    val addTodoViewModel: AddTodoViewModel = viewModel()
    val viewState by addTodoViewModel.states.collectAsState()

    LaunchedEffect(Unit) {
        addTodoViewModel(Init(listId))
        addTodoViewModel.oneOffEvents.collect {
            when (it) {
                is AddToDo -> onToDoAdded(it.listId, it.title, it.due)
                is OnBackClicked -> onBackPressed()
            }.exaustive
        }
    }

    BottomSheet(
        isExpanded = isVisible,
        onDismissed = {
            addTodoViewModel(OnBackPressed)
        },
        onConfirmClicked = {
            addTodoViewModel(DoneClicked)
        },
        onCancelClicked = {
            addTodoViewModel(OnBackPressed)
        },
        sheetContent = {
            ClosableInputText(
                onBackPressed = { addTodoViewModel(OnBackPressed) },
                onDoneClicked = { addTodoViewModel(DoneClicked) },
                onTitleChanged = { addTodoViewModel(OnTitleChanged(it)) },
                text = viewState.title,
                isError = viewState.isError,
                label = stringResource(R.string.new_todo)
            )
        },
        optionalDialogControls = {
            Calendar(
                due = viewState.due,
                onTimeDataPicked = { addTodoViewModel(OnTimeDataPicked(it)) }
            )
        }
    )
}
