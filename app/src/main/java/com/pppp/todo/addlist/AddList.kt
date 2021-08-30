package com.pppp.todo.addlist

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.R
import com.pppp.todo.addlist.AddListViewModel.OneOffEvent.OnSaveClicked
import com.pppp.todo.addlist.AddListViewModel.Event
import com.pppp.todo.addlist.AddListViewModel.Event.OnTextChanged
import com.pppp.todo.addlist.AddListViewModel.Event.OnVisibilityChanged
import com.pppp.todo.exaustive
import com.pppp.uielements.BottomSheet
import com.pppp.uielements.ClosableInputText
import kotlinx.coroutines.flow.collect

internal const val EMPTY_STRING = ""

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun AddList(
    isVisible: Boolean = false,
    onCancel: () -> Unit = {}
) {
    val addListViewModel: AddListViewModel = viewModel()

    LaunchedEffect(isVisible) {
        addListViewModel(OnVisibilityChanged(isVisible = isVisible))
        addListViewModel.oneOffEvents.collect {
            when (it) {
                OnSaveClicked -> onCancel()
            }.exaustive
        }
    }
    val viewState by addListViewModel.states.collectAsState()

    BottomSheet(
        isExpanded = viewState.isVisible,
        onDismissed = onCancel,
        onConfirmClicked = {
            addListViewModel(Event.OnSaveClicked)
        },
        onCancelClicked = onCancel,
        sheetContent = {
            ClosableInputText(
                text = viewState.text,
                onTitleChanged = {
                    addListViewModel(OnTextChanged(it))
                },
                label = stringResource(id = R.string.create_new_list),
                isError = viewState.isError
            )
        }
    )
}
