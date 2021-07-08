package com.pppp.todo.edittodo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pppp.todo.R
import com.pppp.todo.edittodo.EditTodoViewEvent.Init
import com.pppp.todo.edittodo.EditTodoViewEvent.OnBackPressed
import com.pppp.todo.edittodo.EditTodoViewEvent.OnDoneClicked
import com.pppp.todo.edittodo.EditTodoViewEvent.OnTextChanged
import com.pppp.todo.exaustive
import com.pppp.todo.main.viewmodel.MainViewEvent
import com.pppp.todo.main.viewmodel.MainViewEvent.OnCancel
import com.pppp.uielements.BottomSheet
import kotlinx.coroutines.flow.collect

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun EditBottomSheet(
    item: String? = null,
    modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    ),
    onEvent: (MainViewEvent) -> Unit = {}
) {
    val editViewModel = viewModel<EditViewModel>()
    LaunchedEffect(editViewModel, onEvent) {
        editViewModel.oneOffEvents.collect {
            when (it) {
                is OneOffEvents.OnCancel -> onEvent(OnCancel)
            }.exaustive
        }
    }
    LaunchedEffect(item) {
        editViewModel(Init(item))
    }
    val state by editViewModel.states.collectAsState()
    LaunchedEffect(state.isVisible) {
        if (state.isVisible) {
            modalBottomSheetState.show()
        } else {
            modalBottomSheetState.hide()
        }
    }
    BottomSheet(
        onBackPressed = {
            editViewModel(OnBackPressed)
        },
        content = {
            Content(
                state = state
            ) {
                editViewModel(it)
            }
        },
        modalBottomSheetState = modalBottomSheetState
    )
}

@ExperimentalComposeUiApi
@Composable
fun Content(
    state: EditTodoViewState = remember {
        mutableStateOf(EditTodoViewState())
    }.value,
    onEvent: (EditTodoViewEvent) -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 8.dp
        )
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
                            true
                        },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onEvent(OnDoneClicked) }),
                    value = state.title,
                    onValueChange = {
                        onEvent(OnTextChanged(it))
                    }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                modifier = Modifier.padding(end = 8.dp, top = 8.dp),
                onClick = {
                    onEvent(OnBackPressed)
                }) {
                Text(text = stringResource(id = R.string.cancel))
            }
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    onEvent(OnDoneClicked)
                }) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

@Composable
@Preview
@ExperimentalComposeUiApi
private fun ContentPtreview(onEvent: (MainViewEvent) -> Unit = {}) {
    Content()
}




